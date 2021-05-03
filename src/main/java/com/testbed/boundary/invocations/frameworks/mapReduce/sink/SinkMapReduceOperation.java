package com.testbed.boundary.invocations.frameworks.mapReduce.sink;

import com.testbed.boundary.invocations.InvocationParameters;
import com.testbed.boundary.invocations.Operation;
import com.testbed.boundary.invocations.intermediateDatasets.IntermediateDataset;
import com.testbed.boundary.invocations.intermediateDatasets.NoIntermediateDataset;
import lombok.Getter;

import static com.testbed.boundary.invocations.OperationsConstants.SINK;

public class SinkMapReduceOperation implements Operation {
    @Getter
    private final String name = SINK;

    @Override
    public IntermediateDataset invoke(InvocationParameters invocationParameters) {
        return new NoIntermediateDataset();
    }
}
