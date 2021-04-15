package com.testbed.boundary.invocations.mapReduce;

import com.testbed.boundary.invocations.InvocationParameters;
import com.testbed.boundary.invocations.Operation;
import com.testbed.boundary.invocations.intermediateDatasets.IntermediateDataset;
import com.testbed.boundary.invocations.intermediateDatasets.ReferenceIntermediateDataset;
import com.testbed.boundary.utils.ParquetSchemaReader;
import com.testbed.entities.operations.physical.PhysicalGroupBy;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.parquet.example.data.Group;
import org.apache.parquet.example.data.simple.SimpleGroup;
import org.apache.parquet.hadoop.example.ExampleInputFormat;
import org.apache.parquet.hadoop.example.ExampleOutputFormat;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;
import org.apache.parquet.schema.GroupType;
import org.apache.parquet.schema.MessageType;
import org.apache.parquet.schema.MessageTypeParser;
import org.apache.parquet.schema.Type;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.testbed.boundary.invocations.OperationsConstants.GROUP_BY;
import static com.testbed.boundary.invocations.mapReduce.JobConfigurationCommons.PATH_PREFIX;
import static com.testbed.boundary.invocations.mapReduce.JobConfigurationCommons.VERBOSE;

@RequiredArgsConstructor
public class GroupByMapReduceOperation implements Operation {
    private static final int FIRST = 0;
    private static final String GROUP_BY_COLUMN_INDEXES = "groupByColumnIndexes";
    private static final String GROUP_BY_SCHEMA = "groupBySchema";
    private final JobConfigurationCommons jobConfigurationCommons;
    private final ParquetSchemaReader parquetSchemaReader;
    @Getter
    private final String name = GROUP_BY;

    @Override
    public IntermediateDataset invoke(InvocationParameters invocationParameters) {
        try {
            return tryRunJob(invocationParameters);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private ReferenceIntermediateDataset tryRunJob(InvocationParameters invocationParameters) throws IOException,
            InterruptedException, ClassNotFoundException {
        PhysicalGroupBy physicalGroupBy = (PhysicalGroupBy) invocationParameters.getPhysicalOperation();
        String inputPath = invocationParameters.getInputIntermediateDatasets().get(FIRST)
                .getValue()
                .get()
                .toString();
        String outputPath = PATH_PREFIX + physicalGroupBy.getId();
        Job job = jobConfigurationCommons.createMapperReducerJob(JobConfiguration.builder()
                .inputPath(inputPath)
                .outputPath(outputPath)
                .inputFormatClass(ExampleInputFormat.class)
                .outputFormatClass(ExampleOutputFormat.class)
                .mapOutputKeyClass(Text.class)
                .mapOutputValueClass(NullWritable.class)
                .outputKeyClass(LongWritable.class)
                .outputValueClass(Group.class)
                .mapperClass(GroupByMapper.class)
                .reducerClass(GroupByReducer.class)
                .build());
        MessageType inputSchema = parquetSchemaReader.readSchema(inputPath);
        MessageType groupBySchema = getGroupBySchema(physicalGroupBy.getGroupingColumnNames(), inputSchema);
        ExampleOutputFormat.setSchema(job, groupBySchema);
        ExampleOutputFormat.setCompression(job, CompressionCodecName.UNCOMPRESSED);
        String[] groupByColumnIndexes = getGroupByColumnIndexes(physicalGroupBy.getGroupingColumnNames(), inputSchema);
        job.getConfiguration().setStrings(GROUP_BY_COLUMN_INDEXES, groupByColumnIndexes);
        job.getConfiguration().set(GROUP_BY_SCHEMA, groupBySchema.toString());
        job.waitForCompletion(VERBOSE);
        return new ReferenceIntermediateDataset(outputPath);
    }

    private MessageType getGroupBySchema(List<String> columnNames, MessageType schema) {
        Set<String> columnNamesSet = new HashSet<>(columnNames);
        List<Type> projectedFields = schema.getFields().stream()
                .filter(field -> columnNamesSet.contains(field.getName()))
                .collect(Collectors.toList());
        return new MessageType(schema.getName(), projectedFields);
    }

    private String[] getGroupByColumnIndexes(List<String> columnNames, MessageType schema) {
        return columnNames.stream()
                .map(schema::getFieldIndex)
                .map(String::valueOf)
                .toArray(String[]::new);
    }

    private static class GroupByMapper extends Mapper<LongWritable, Group, Text, NullWritable> {
        private static final int DEFAULT_POSITION = 0;
        private static final String TYPE_DELIMITER = ":";
        private static final CharSequence COLUMN_NAME_VALUES_DELIMITER = ",";

        @Override
        public void map(LongWritable key, Group value, Context context) throws IOException, InterruptedException {
            int[] groupingColumnIndexes = context.getConfiguration().getInts(GROUP_BY_COLUMN_INDEXES);
            GroupType originalGroupType = value.getType();
            String groupByColumnNameValues = Arrays.stream(groupingColumnIndexes)
                    .mapToObj(originalGroupType::getType)
                    .map(Type::getName)
                    .map(groupingColumnName -> groupingColumnName + TYPE_DELIMITER + value.getString(groupingColumnName, DEFAULT_POSITION))
                    .collect(Collectors.joining(COLUMN_NAME_VALUES_DELIMITER));
            context.write(new Text(groupByColumnNameValues), NullWritable.get());
        }
    }

    private static class GroupByReducer extends Reducer<Text, NullWritable, LongWritable, Group> {
        private static final String TYPE_DELIMITER = ":";
        private static final String COLUMN_NAME_VALUES_DELIMITER = ",";

        @Override
        public void reduce(Text key, Iterable<NullWritable> notUsed, Context context) throws IOException, InterruptedException {
            String groupBySchemaString = context.getConfiguration().get(GROUP_BY_SCHEMA);
            MessageType groupBySchema = MessageTypeParser.parseMessageType(groupBySchemaString);
            GroupType groupByGroupType = new GroupType(groupBySchema.getRepetition(), groupBySchema.getName(), groupBySchema.getFields());
            Group groupByGroup = new SimpleGroup(groupByGroupType);
            String[] groupByColumnNameValues = key.toString().split(COLUMN_NAME_VALUES_DELIMITER);
            Arrays.stream(groupByColumnNameValues)
                    .map(columnNameValue -> columnNameValue.split(TYPE_DELIMITER))
                    .forEach(columnNameAndValue -> groupByGroup.append(columnNameAndValue[0], columnNameAndValue[1]));
            context.write(null, groupByGroup);
        }
    }
}
