package com.testbed.boundary.invocations.mapReduce;

import com.testbed.boundary.invocations.InvocationParameters;
import com.testbed.boundary.invocations.Invokable;
import com.testbed.boundary.invocations.Nameable;
import com.testbed.boundary.invocations.intermediateDatasets.IntermediateDataset;
import com.testbed.boundary.invocations.intermediateDatasets.NoIntermediateDataset;
import com.testbed.boundary.invocations.intermediateDatasets.ReferenceIntermediateDataset;
import com.testbed.entities.operations.physical.PhysicalLoad;
import com.testbed.entities.operations.physical.PhysicalSelect;
import com.testbed.entities.operations.physical.PhysicalSink;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.parquet.example.data.Group;
import org.apache.parquet.hadoop.example.ExampleInputFormat;
import org.apache.parquet.hadoop.example.ExampleOutputFormat;

import java.io.IOException;

import static com.testbed.boundary.invocations.OperationsConstants.LOAD;
import static com.testbed.boundary.invocations.OperationsConstants.SELECT;
import static com.testbed.boundary.invocations.OperationsConstants.SINK;
import static com.testbed.boundary.invocations.mapReduce.JobConfigurationCommons.PATH_PREFIX;
import static com.testbed.boundary.invocations.mapReduce.JobConfigurationCommons.VERBOSE;

@RequiredArgsConstructor
public class SinkDebugMapReduceOperation implements Invokable, Nameable {
    private final JobConfigurationCommons jobConfigurationCommons;
    @Getter
    private final String name = SINK;

    @Override
    public IntermediateDataset invoke(InvocationParameters invocationParameters) {
        try {
            return tryRunJob(invocationParameters);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private IntermediateDataset tryRunJob(InvocationParameters invocationParameters) throws IOException, InterruptedException, ClassNotFoundException {
        PhysicalSink physicalSink = (PhysicalSink) invocationParameters.getPhysicalOperation();
        String inputPath = invocationParameters.getInputIntermediateDatasets().stream()
                .findFirst()
                .get()
                .getValue()
                .toString();
        String outputPath = PATH_PREFIX + physicalSink.getId();
        jobConfigurationCommons.createMapperOnlyJob(JobConfiguration.builder()
                .inputPath(inputPath)
                .outputPath(outputPath)
                .inputFormatClass(ExampleInputFormat.class)
                .outputFormatClass(TextOutputFormat.class)
                .jarByClass(SinkMapper.class)
                .mapperClass(SinkMapper.class)
                .build()).waitForCompletion(VERBOSE);
        return new NoIntermediateDataset();
    }

    private static class SinkMapper extends Mapper<LongWritable, Group, LongWritable, Group> {
        public void map(LongWritable key, Group value, Context context) throws IOException, InterruptedException {
            context.write(key, value);
        }
    }
}
