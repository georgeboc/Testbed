package com.testbed.boundary.invocations.frameworks.mapReduce.sumAggregator;

import com.testbed.boundary.invocations.InvocationParameters;
import com.testbed.boundary.invocations.Operation;
import com.testbed.boundary.invocations.frameworks.mapReduce.JobConfigurationCommons;
import com.testbed.boundary.invocations.frameworks.mapReduce.UnaryOperationJobConfiguration;
import com.testbed.boundary.invocations.intermediateDatasets.IntermediateDataset;
import com.testbed.boundary.invocations.intermediateDatasets.ReferenceIntermediateDataset;
import com.testbed.entities.operations.physical.PhysicalAggregate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.parquet.example.data.Group;
import org.apache.parquet.hadoop.example.ExampleInputFormat;
import org.apache.parquet.hadoop.example.ExampleOutputFormat;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;
import org.apache.parquet.schema.MessageType;
import org.apache.parquet.schema.PrimitiveType;
import org.apache.parquet.schema.Type;

import java.io.IOException;

import static com.testbed.boundary.invocations.OperationsConstants.AGGREGATE;
import static com.testbed.boundary.invocations.frameworks.mapReduce.JobConfigurationCommons.INTERMEDIATE_DATASETS_DIRECTORY_PREFIX;
import static com.testbed.boundary.invocations.frameworks.mapReduce.JobConfigurationCommons.VERBOSE;

@RequiredArgsConstructor
public class SumAggregateMapReduceOperation implements Operation {
    private static final int FIRST = 0;
    private static final String AGGREGATE_COLUMN_NAME = "aggregateColumnName";
    private static final String SCHEMA_NAME = "aggregationSchema";
    private static final String SUM_PREFIX = "Sum";

    private final JobConfigurationCommons jobConfigurationCommons;
    @Getter
    private final String name = AGGREGATE;

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
        PhysicalAggregate physicalAggregate = (PhysicalAggregate) invocationParameters.getPhysicalOperation();
        String inputPath = invocationParameters.getInputIntermediateDatasets().get(FIRST)
                .getValue()
                .get()
                .toString();
        String outputPath = INTERMEDIATE_DATASETS_DIRECTORY_PREFIX + physicalAggregate.getId();
        Job job = jobConfigurationCommons.createMapperCombinerReducerJobWithUnaryInputs(UnaryOperationJobConfiguration.builder()
                .inputPath(inputPath)
                .outputPath(outputPath)
                .inputFormatClass(ExampleInputFormat.class)
                .outputFormatClass(ExampleOutputFormat.class)
                .mapOutputKeyClass(NullWritable.class)
                .mapOutputValueClass(DoubleWritable.class)
                .outputKeyClass(LongWritable.class)
                .outputValueClass(Group.class)
                .mapperClass(SumAggregateJar.SumAggregateMapper.class)
                .combinerClass(SumAggregateJar.SumAggregateCombiner.class)
                .reducerClass(SumAggregateJar.SumAggregateReducer.class)
                .jar(SumAggregateJar.class)
                .build());
        MessageType aggregateSchema = getAggregateSchema(physicalAggregate);
        ExampleOutputFormat.setSchema(job, aggregateSchema);
        ExampleOutputFormat.setCompression(job, CompressionCodecName.UNCOMPRESSED);
        job.getConfiguration().set(AGGREGATE_COLUMN_NAME, physicalAggregate.getAggregationColumnName());
        job.waitForCompletion(VERBOSE);
        return new ReferenceIntermediateDataset(outputPath);
    }

    private MessageType getAggregateSchema(PhysicalAggregate physicalAggregate) {
        String sumAggregatedColumnName = SUM_PREFIX + physicalAggregate.getAggregationColumnName();
        Type sumAggregatedColumn = new PrimitiveType(Type.Repetition.OPTIONAL,
                PrimitiveType.PrimitiveTypeName.BINARY,
                sumAggregatedColumnName);
        return new MessageType(SCHEMA_NAME, sumAggregatedColumn);
    }
}
