package com.testbed.boundary.invocations.frameworks.mapReduce.join;

import com.google.common.collect.Streams;
import com.testbed.boundary.invocations.InvocationParameters;
import com.testbed.boundary.invocations.Operation;
import com.testbed.boundary.invocations.frameworks.mapReduce.BinaryOperationJobConfiguration;
import com.testbed.boundary.invocations.frameworks.mapReduce.JobConfigurationCommons;
import com.testbed.boundary.invocations.intermediateDatasets.IntermediateDataset;
import com.testbed.boundary.invocations.intermediateDatasets.ReferenceIntermediateDataset;
import com.testbed.boundary.utils.ParquetSchemaReader;
import com.testbed.entities.operations.physical.PhysicalJoin;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.parquet.example.data.Group;
import org.apache.parquet.hadoop.example.ExampleInputFormat;
import org.apache.parquet.hadoop.example.ExampleOutputFormat;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;
import org.apache.parquet.schema.MessageType;
import org.apache.parquet.schema.PrimitiveType;
import org.apache.parquet.schema.Type;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.testbed.boundary.invocations.OperationsConstants.JOIN;
import static com.testbed.boundary.invocations.frameworks.mapReduce.JobConfigurationCommons.INTERMEDIATE_DATASETS_DIRECTORY_PREFIX;
import static com.testbed.boundary.invocations.frameworks.mapReduce.JobConfigurationCommons.VERBOSE;

@RequiredArgsConstructor
public class JoinMapReduceOperation implements Operation {
    private static final int LEFT_POSITION = 0;
    private static final int RIGHT_POSITION = 1;
    private static final String JOIN_SCHEMA = "joinSchema";
    private static final String JOINED_SCHEMA_NAME = "joinedSchema";
    private static final String LEFT_PREFIX = "Left";
    private static final String RIGHT_PREFIX = "Right";
    private static final String LEFT_SOURCE_JOIN_COLUMN_INDEX = "leftSourceJoinColumnIndex";
    private static final String RIGHT_SOURCE_JOIN_COLUMN_INDEX = "rightSourceJoinColumnIndex";

    private final JobConfigurationCommons jobConfigurationCommons;
    private final ParquetSchemaReader parquetSchemaReader;
    @Getter
    private final String name = JOIN;

    @Override
    public IntermediateDataset invoke(final InvocationParameters invocationParameters) {
        try {
            return tryRunJob(invocationParameters);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private ReferenceIntermediateDataset tryRunJob(final InvocationParameters invocationParameters) throws IOException,
            InterruptedException, ClassNotFoundException {
        PhysicalJoin physicalJoin = (PhysicalJoin) invocationParameters.getPhysicalOperation();
        String leftInputPath = getInputPath(invocationParameters, LEFT_POSITION);
        String rightInputPath = getInputPath(invocationParameters, RIGHT_POSITION);
        String outputPath = INTERMEDIATE_DATASETS_DIRECTORY_PREFIX + physicalJoin.getId();
        Job job = jobConfigurationCommons.createMapperReducerJobWithBinaryInputs(BinaryOperationJobConfiguration.builder()
                .leftInputPath(leftInputPath)
                .rightInputPath(rightInputPath)
                .outputPath(outputPath)
                .leftInputFormatClass(ExampleInputFormat.class)
                .rightInputFormatClass(ExampleInputFormat.class)
                .outputFormatClass(ExampleOutputFormat.class)
                .mapOutputKeyClass(Text.class)
                .mapOutputValueClass(Text.class)
                .outputKeyClass(LongWritable.class)
                .outputValueClass(Group.class)
                .leftMapperClass(JoinJar.LeftSourceJoinMapper.class)
                .rightMapperClass(JoinJar.RightSourceJoinMapper.class)
                .reducerClass(JoinJar.JoinReducer.class)
                .jar(JoinJar.class)
                .build());
        MessageType leftInputSchema = parquetSchemaReader.readSchema(leftInputPath);
        MessageType rightInputSchema = parquetSchemaReader.readSchema(rightInputPath);
        MessageType joinSchema = getJoinSchema(leftInputSchema, rightInputSchema);
        ExampleOutputFormat.setSchema(job, joinSchema);
        ExampleOutputFormat.setCompression(job, CompressionCodecName.UNCOMPRESSED);
        int leftSourceJoinColumnIndex = leftInputSchema.getFieldIndex(physicalJoin.getJoinLeftColumnName());
        int rightSourceJoinColumnIndex = rightInputSchema.getFieldIndex(physicalJoin.getJoinRightColumnName());
        job.getConfiguration().setInt(LEFT_SOURCE_JOIN_COLUMN_INDEX, leftSourceJoinColumnIndex);
        job.getConfiguration().setInt(RIGHT_SOURCE_JOIN_COLUMN_INDEX, rightSourceJoinColumnIndex);
        job.getConfiguration().set(JOIN_SCHEMA, joinSchema.toString());
        job.waitForCompletion(VERBOSE);
        return new ReferenceIntermediateDataset(outputPath);
    }

    private String getInputPath(final InvocationParameters invocationParameters, final int position) {
        return invocationParameters.getInputIntermediateDatasets().get(position)
                .getValue()
                .get()
                .toString();
    }

    private MessageType getJoinSchema(final MessageType leftSchema, final MessageType rightSchema) {
        List<Type> leftSchemaFields = getSchemaFields(leftSchema, LEFT_PREFIX);
        List<Type> rightSchemaFields = getSchemaFields(rightSchema, RIGHT_PREFIX);
        List<Type> joinedFields = Streams.concat(leftSchemaFields.stream(), rightSchemaFields.stream())
                .collect(Collectors.toList());
        return new MessageType(JOINED_SCHEMA_NAME, joinedFields);
    }

    private List<Type> getSchemaFields(final MessageType leftSchema, final String fieldNamePrefix) {
        return leftSchema.getFields().stream()
                .map(field -> new PrimitiveType(field.getRepetition(),
                        field.asPrimitiveType().getPrimitiveTypeName(),
                        fieldNamePrefix + field.getName()))
                .collect(Collectors.toList());
    }
}
