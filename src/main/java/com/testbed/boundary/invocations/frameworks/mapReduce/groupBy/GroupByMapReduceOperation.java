package com.testbed.boundary.invocations.frameworks.mapReduce.groupBy;

import com.testbed.boundary.invocations.InvocationParameters;
import com.testbed.boundary.invocations.Operation;
import com.testbed.boundary.invocations.frameworks.mapReduce.JobConfigurationCommons;
import com.testbed.boundary.invocations.frameworks.mapReduce.UnaryOperationJobConfiguration;
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
import org.apache.parquet.example.data.Group;
import org.apache.parquet.hadoop.example.ExampleInputFormat;
import org.apache.parquet.hadoop.example.ExampleOutputFormat;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;
import org.apache.parquet.schema.MessageType;
import org.apache.parquet.schema.Type;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.testbed.boundary.invocations.OperationsConstants.GROUP_BY;
import static com.testbed.boundary.invocations.frameworks.mapReduce.JobConfigurationCommons.LOCAL_DIRECTORY_PREFIX;
import static com.testbed.boundary.invocations.frameworks.mapReduce.JobConfigurationCommons.VERBOSE;

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
        String outputPath = LOCAL_DIRECTORY_PREFIX + physicalGroupBy.getId();
        Job job = jobConfigurationCommons.createMapperReducerJobWithUnaryInputs(UnaryOperationJobConfiguration.builder()
                .inputPath(inputPath)
                .outputPath(outputPath)
                .inputFormatClass(ExampleInputFormat.class)
                .outputFormatClass(ExampleOutputFormat.class)
                .mapOutputKeyClass(Text.class)
                .mapOutputValueClass(NullWritable.class)
                .outputKeyClass(LongWritable.class)
                .outputValueClass(Group.class)
                .mapperClass(GroupByJar.GroupByMapper.class)
                .reducerClass(GroupByJar.GroupByReducer.class)
                .jar(GroupByJar.class)
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
}
