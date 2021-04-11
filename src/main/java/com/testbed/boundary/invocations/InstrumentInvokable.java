package com.testbed.boundary.invocations;

import com.testbed.boundary.invocations.intermediateDatasets.IntermediateDataset;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class InstrumentInvokable implements Invokable {
    private final Invokable wrappedInvokable;
    private final List<OperationInstrumentation> operationInstrumentations;

    @Override
    public IntermediateDataset invoke(final InvocationParameters invocationParameters) {
        IntermediateDataset intermediateDataset = wrappedInvokable.invoke(invocationParameters);
        Nameable nameable = (Nameable) wrappedInvokable;
        List<Long> inputsRowsCounts = getRowsCounts(invocationParameters.getInputIntermediateDatasets());
        long outputRowsCount = intermediateDataset.count();
        List<List<String>> inputsColumnNames = getColumnNames(invocationParameters.getInputIntermediateDatasets());
        List<String> outputColumnNames = intermediateDataset.getColumnNames();
        operationInstrumentations.add(OperationInstrumentation.builder()
                .operationName(nameable.getName())
                .inputsRowsCount(inputsRowsCounts)
                .outputRowsCount(outputRowsCount)
                .inputsColumnNames(inputsColumnNames)
                .outputColumnNames(outputColumnNames)
                .build());
        return intermediateDataset;
    }

    private List<Long> getRowsCounts(final Collection<IntermediateDataset> inputIntermediateDatasets) {
        return inputIntermediateDatasets.stream()
                .map(IntermediateDataset::count)
                .collect(Collectors.toList());
    }

    private List<List<String>> getColumnNames(final Collection<IntermediateDataset> inputIntermediateDatasets) {
        return inputIntermediateDatasets.stream()
                .map(IntermediateDataset::getColumnNames)
                .collect(Collectors.toList());
    }
}
