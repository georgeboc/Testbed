package com.testbed.boundary.invocations.spark;

import com.testbed.boundary.invocations.InvocationParameters;
import com.testbed.boundary.invocations.Invokable;
import com.testbed.boundary.invocations.Nameable;
import com.testbed.boundary.invocations.intermediateDatasets.IntermediateDataset;
import com.testbed.boundary.invocations.intermediateDatasets.NoIntermediateDataset;
import com.testbed.boundary.invocations.intermediateDatasets.instrumentation.SparkIntermediateDatasetInstrumentation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.testbed.boundary.invocations.OperationsConstants.SINK;

@RequiredArgsConstructor
public class SinkSparkOperation implements Invokable, Nameable {
    private final SparkIntermediateDatasetInstrumentation sparkIntermediateDatasetInstrumentation;
    @Getter
    private final String name = SINK;

    @Override
    public IntermediateDataset invoke(final InvocationParameters invocationParameters) {
        IntermediateDataset inputIntermediateDataset = invocationParameters.getInputIntermediateDatasets().stream().findFirst().get();
        sparkIntermediateDatasetInstrumentation.count(inputIntermediateDataset);
        return new NoIntermediateDataset();
    }
}
