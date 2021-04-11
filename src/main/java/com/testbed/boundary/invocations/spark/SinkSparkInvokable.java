package com.testbed.boundary.invocations.spark;

import com.testbed.boundary.invocations.Invokable;
import com.testbed.boundary.invocations.InvocationParameters;
import com.testbed.boundary.invocations.intermediateDatasets.NoIntermediateDataset;
import com.testbed.boundary.invocations.intermediateDatasets.IntermediateDataset;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SinkSparkInvokable implements Invokable {
    @Override
    public IntermediateDataset invoke(final InvocationParameters invocationParameters) {
        IntermediateDataset inputIntermediateDataset = invocationParameters.getInputIntermediateDatasets().stream().findFirst().get();
        inputIntermediateDataset.count();
        return new NoIntermediateDataset();
    }
}
