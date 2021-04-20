package com.testbed.boundary.invocations.spark;

import com.testbed.boundary.invocations.InvocationParameters;
import com.testbed.boundary.invocations.Operation;
import com.testbed.boundary.invocations.intermediateDatasets.IntermediateDataset;
import com.testbed.boundary.invocations.intermediateDatasets.NoIntermediateDataset;
import com.testbed.boundary.invocations.intermediateDatasets.instrumentation.IntermediateDatasetInstrumentation;
import com.testbed.boundary.invocations.intermediateDatasets.instrumentation.SparkIntermediateDatasetInstrumentation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.testbed.boundary.invocations.OperationsConstants.SINK;

@RequiredArgsConstructor
public class SinkSparkOperation implements Operation {
    private final IntermediateDatasetInstrumentation intermediateDatasetInstrumentation;
    @Getter
    private final String name = SINK;

    @Override
    public IntermediateDataset invoke(final InvocationParameters invocationParameters) {
        IntermediateDataset inputIntermediateDataset = invocationParameters.getInputIntermediateDatasets().stream().findFirst().get();
        intermediateDatasetInstrumentation.count(inputIntermediateDataset);
        return new NoIntermediateDataset();
    }
}
