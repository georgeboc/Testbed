package com.testbed.boundary.invocations.mapReduce;

import com.testbed.boundary.invocations.InvocationParameters;
import com.testbed.boundary.invocations.Invokable;
import com.testbed.boundary.invocations.Nameable;
import com.testbed.boundary.invocations.intermediateDatasets.IntermediateDataset;
import com.testbed.boundary.invocations.intermediateDatasets.NoIntermediateDataset;
import lombok.Getter;
import org.apache.hadoop.mapreduce.Job;
import org.apache.parquet.hadoop.example.ExampleInputFormat;
import org.apache.parquet.hadoop.example.ExampleOutputFormat;

import static com.testbed.boundary.invocations.OperationsConstants.SINK;

public class SinkMapReduceOperation implements Invokable, Nameable {
    @Getter
    private final String name = SINK;

    @Override
    public IntermediateDataset invoke(InvocationParameters invocationParameters) {
        return new NoIntermediateDataset();
    }
}
