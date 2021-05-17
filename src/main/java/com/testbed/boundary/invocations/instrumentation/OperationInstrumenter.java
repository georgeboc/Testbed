package com.testbed.boundary.invocations.instrumentation;

import com.testbed.boundary.invocations.InvocationParameters;
import com.testbed.boundary.invocations.Operation;
import com.testbed.boundary.invocations.intermediateDatasets.IntermediateDataset;
import com.testbed.boundary.invocations.intermediateDatasets.instrumentation.IntermediateDatasetInstrumentation;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class OperationInstrumenter implements Operation {
    private final Operation wrappedOperation;
    private final IntermediateDatasetInstrumentation intermediateDatasetInstrumentation;
    private final List<OperationInstrumentation> operationInstrumentations;

    @Override
    public IntermediateDataset invoke(final InvocationParameters invocationParameters) {
        IntermediateDataset intermediateDataset = wrappedOperation.invoke(invocationParameters);
        List<Long> inputsRowsCounts = getRowsCounts(invocationParameters.getInputIntermediateDatasets());
        long outputRowsCount = intermediateDatasetInstrumentation.count(intermediateDataset);
        List<List<String>> inputsColumnNames = getColumnNames(invocationParameters.getInputIntermediateDatasets());
        List<String> outputColumnNames = intermediateDatasetInstrumentation.getColumnNames(intermediateDataset);
        operationInstrumentations.add(OperationInstrumentation.builder()
                .operationName(wrappedOperation.getName())
                .inputsRowsCount(inputsRowsCounts)
                .outputRowsCount(outputRowsCount)
                .inputsColumnNames(inputsColumnNames)
                .outputColumnNames(outputColumnNames)
                .build());
        return intermediateDataset;
    }

    private List<Long> getRowsCounts(final Collection<IntermediateDataset> inputIntermediateDatasets) {
        return inputIntermediateDatasets.stream()
                .map(intermediateDatasetInstrumentation::count)
                .collect(Collectors.toList());
    }

    private List<List<String>> getColumnNames(final Collection<IntermediateDataset> inputIntermediateDatasets) {
        return inputIntermediateDatasets.stream()
                .map(intermediateDatasetInstrumentation::getColumnNames)
                .collect(Collectors.toList());
    }

    @Override
    public String getName() {
        return wrappedOperation.getName();
    }
}
