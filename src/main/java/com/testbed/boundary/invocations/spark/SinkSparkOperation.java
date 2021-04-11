package com.testbed.boundary.invocations.spark;

import com.testbed.boundary.invocations.Invokable;
import com.testbed.boundary.invocations.InvocationParameters;
import com.testbed.boundary.invocations.Nameable;
import com.testbed.boundary.invocations.intermediateDatasets.NoIntermediateDataset;
import com.testbed.boundary.invocations.intermediateDatasets.IntermediateDataset;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SinkSparkOperation implements Invokable, Nameable {
    private static final String SINK = "Sink";

    @Override
    public IntermediateDataset invoke(final InvocationParameters invocationParameters) {
        IntermediateDataset inputIntermediateDataset = invocationParameters.getInputIntermediateDatasets().stream().findFirst().get();
        inputIntermediateDataset.count();
        return new NoIntermediateDataset();
    }

    @Override
    public String getName() {
        return SINK;
    }
}
