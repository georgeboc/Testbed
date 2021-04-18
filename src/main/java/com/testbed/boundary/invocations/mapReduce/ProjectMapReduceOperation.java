package com.testbed.boundary.invocations.mapReduce;

import com.testbed.boundary.invocations.InvocationParameters;
import com.testbed.boundary.invocations.Operation;
import com.testbed.boundary.invocations.intermediateDatasets.IntermediateDataset;
import com.testbed.boundary.invocations.intermediateDatasets.ReferenceIntermediateDataset;
import com.testbed.boundary.utils.ParquetSchemaReader;
import com.testbed.entities.operations.physical.PhysicalProject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.parquet.example.data.Group;
import org.apache.parquet.example.data.simple.SimpleGroup;
import org.apache.parquet.hadoop.example.ExampleInputFormat;
import org.apache.parquet.hadoop.example.ExampleOutputFormat;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;
import org.apache.parquet.schema.GroupType;
import org.apache.parquet.schema.MessageType;
import org.apache.parquet.schema.Type;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.testbed.boundary.invocations.OperationsConstants.PROJECT;
import static com.testbed.boundary.invocations.mapReduce.JobConfigurationCommons.PATH_PREFIX;
import static com.testbed.boundary.invocations.mapReduce.JobConfigurationCommons.VERBOSE;

@RequiredArgsConstructor
public class ProjectMapReduceOperation implements Operation {
    private static final int FIRST = 0;
    private static final String PROJECTED_COLUMN_INDEXES = "projectedColumnIndexes";
    private final JobConfigurationCommons jobConfigurationCommons;
    private final ParquetSchemaReader parquetSchemaReader;
    @Getter
    private final String name = PROJECT;

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
        PhysicalProject physicalProject = (PhysicalProject) invocationParameters.getPhysicalOperation();
        String inputPath = invocationParameters.getInputIntermediateDatasets().get(FIRST)
                .getValue()
                .get()
                .toString();
        String outputPath = PATH_PREFIX + physicalProject.getId();
        Job job = jobConfigurationCommons.createMapperOnlyJobWithUnaryInputs(UnaryOperationJobConfiguration.builder()
                .inputPath(inputPath)
                .outputPath(outputPath)
                .inputFormatClass(ExampleInputFormat.class)
                .outputFormatClass(ExampleOutputFormat.class)
                .outputKeyClass(LongWritable.class)
                .outputValueClass(Group.class)
                .mapperClass(ProjectMapper.class)
                .build());
        MessageType schema = parquetSchemaReader.readSchema(inputPath);
        MessageType projectedSchema = getProjectedSchema(physicalProject.getProjectedColumnNames(), schema);
        ExampleOutputFormat.setSchema(job, projectedSchema);
        ExampleOutputFormat.setCompression(job, CompressionCodecName.UNCOMPRESSED);
        String[] projectedColumnIndexes = getProjectedColumnIndexes(physicalProject.getProjectedColumnNames(), schema);
        job.getConfiguration().setStrings(PROJECTED_COLUMN_INDEXES, projectedColumnIndexes);
        job.waitForCompletion(VERBOSE);
        return new ReferenceIntermediateDataset(outputPath);
    }

    private MessageType getProjectedSchema(List<String> columnNames, MessageType schema) {
        Set<String> columnNamesSet = new HashSet<>(columnNames);
        List<Type> projectedFields = schema.getFields().stream()
                .filter(field -> columnNamesSet.contains(field.getName()))
                .sorted((Comparator.comparing(Type::getName)))
                .collect(Collectors.toList());
        return new MessageType(schema.getName(), projectedFields);
    }

    private String[] getProjectedColumnIndexes(List<String> columnNames, MessageType schema) {
        return columnNames.stream()
                .map(schema::getFieldIndex)
                .map(String::valueOf)
                .toArray(String[]::new);
    }

    private static class ProjectMapper extends Mapper<LongWritable, Group, LongWritable, Group> {
        private static final String PROJECTED_COLUMNS = "ProjectedColumns";
        private static final int DEFAULT_POSITION = 0;

        @Override
        public void map(LongWritable key, Group value, Context context) throws IOException, InterruptedException {
            int[] projectedColumnIndexes = context.getConfiguration().getInts(PROJECTED_COLUMN_INDEXES);
            GroupType originalGroupType = value.getType();
            List<Type> projectedColumns = Arrays.stream(projectedColumnIndexes)
                    .mapToObj(originalGroupType::getType)
                    .collect(Collectors.toList());
            GroupType projectedGroupType = new GroupType(originalGroupType.getRepetition(), PROJECTED_COLUMNS, projectedColumns);
            Group projectedGroup = new SimpleGroup(projectedGroupType);
            projectedColumns.stream()
                    .map(Type::getName)
                    .forEach(projectedColumnName -> projectedGroup.append(projectedColumnName,
                            value.getString(projectedColumnName, DEFAULT_POSITION)));
            context.write(key, projectedGroup);
        }
    }
}
