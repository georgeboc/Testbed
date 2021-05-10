package com.testbed.boundary.invocations.frameworks.mapReduce.union;

import com.testbed.boundary.invocations.InvocationParameters;
import com.testbed.boundary.invocations.Operation;
import com.testbed.boundary.invocations.frameworks.mapReduce.BinaryOperationJobConfiguration;
import com.testbed.boundary.invocations.frameworks.mapReduce.JobConfigurationCommons;
import com.testbed.boundary.invocations.intermediateDatasets.IntermediateDataset;
import com.testbed.boundary.invocations.intermediateDatasets.ReferenceIntermediateDataset;
import com.testbed.boundary.utils.ParquetSchemaReader;
import com.testbed.entities.operations.physical.PhysicalUnion;
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

import java.io.IOException;

import static com.testbed.boundary.invocations.OperationsConstants.UNION;
import static com.testbed.boundary.invocations.frameworks.mapReduce.JobConfigurationCommons.MAPREDUCE_EXECUTION_PREFIX;
import static com.testbed.boundary.invocations.frameworks.mapReduce.JobConfigurationCommons.VERBOSE;

@RequiredArgsConstructor
public class UnionMapReduceOperation implements Operation {
    private static final int LEFT_POSITION = 0;
    private static final int RIGHT_POSITION = 1;
    private static final String UNION_SCHEMA = "unionSchema";

    private final JobConfigurationCommons jobConfigurationCommons;
    private final ParquetSchemaReader parquetSchemaReader;
    @Getter
    private final String name = UNION;

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
        PhysicalUnion physicalUnion = (PhysicalUnion) invocationParameters.getPhysicalOperation();
        String leftInputPath = getInputPath(invocationParameters, LEFT_POSITION);
        String rightInputPath = getInputPath(invocationParameters, RIGHT_POSITION);
        String outputPath = MAPREDUCE_EXECUTION_PREFIX + physicalUnion.getId();
        Job job = jobConfigurationCommons.createMapperCombinerReducerJobWithBinaryInputs(BinaryOperationJobConfiguration.builder()
                .leftInputPath(leftInputPath)
                .rightInputPath(rightInputPath)
                .outputPath(outputPath)
                .leftInputFormatClass(ExampleInputFormat.class)
                .rightInputFormatClass(ExampleInputFormat.class)
                .outputFormatClass(ExampleOutputFormat.class)
                .mapOutputKeyClass(Text.class)
                .mapOutputValueClass(NullWritable.class)
                .outputKeyClass(LongWritable.class)
                .outputValueClass(Group.class)
                .leftMapperClass(UnionJar.LeftSourceUnionMapper.class)
                .rightMapperClass(UnionJar.RightSourceUnionMapper.class)
                .combinerClass(UnionJar.UnionCombiner.class)
                .reducerClass(UnionJar.UnionReducer.class)
                .jar(UnionJar.class)
                .build());
        MessageType leftInputSchema = parquetSchemaReader.readSchema(leftInputPath);
        ExampleOutputFormat.setSchema(job, leftInputSchema);
        ExampleOutputFormat.setCompression(job, CompressionCodecName.UNCOMPRESSED);
        job.getConfiguration().set(UNION_SCHEMA, leftInputSchema.toString());
        job.waitForCompletion(VERBOSE);
        return new ReferenceIntermediateDataset(outputPath);
    }

    private String getInputPath(InvocationParameters invocationParameters, int position) {
        return invocationParameters.getInputIntermediateDatasets().get(position)
                .getValue()
                .get()
                .toString();
    }
}
