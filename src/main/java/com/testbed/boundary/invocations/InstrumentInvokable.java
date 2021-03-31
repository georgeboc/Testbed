package com.testbed.boundary.invocations;

import com.testbed.boundary.invocations.results.Result;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class InstrumentInvokable implements Invokable {
    private final static String INVOKABLE = "Invokable";
    private final static String EMPTY = "";

    private final Invokable wrappedInvokable;
    private final List<OperationInstrumentation> operationInstrumentations;

    @Override
    public Result invoke(final InvocationParameters invocationParameters) {
        Instant instantBeforeInvocation = Instant.now();
        Result result = wrappedInvokable.invoke(invocationParameters);
        Instant instantAfterInvocation = Instant.now();
        Duration invocationDuration = Duration.between(instantBeforeInvocation, instantAfterInvocation);
        String operationName = wrappedInvokable.getClass().getSimpleName().replace(INVOKABLE, EMPTY);
        List<Long> inputsRowsCounts = getRowsCounts(invocationParameters.getInputResults());
        long outputRowsCount = result.count();
        List<List<String>> inputsColumnNames = getColumnNames(invocationParameters.getInputResults());
        List<String> outputColumnNames = result.getColumnNames();
        operationInstrumentations.add(OperationInstrumentation.builder()
                .instantBeforeInvocation(instantBeforeInvocation)
                .instantAfterInvocation(instantAfterInvocation)
                .invocationDuration(invocationDuration)
                .operationName(operationName)
                .inputsRowsCount(inputsRowsCounts)
                .outputRowsCount(outputRowsCount)
                .inputsColumnNames(inputsColumnNames)
                .outputColumnNames(outputColumnNames)
                .build());
        return result;
    }

    private List<Long> getRowsCounts(Collection<Result> inputResults) {
        return inputResults.stream()
                .map(Result::count)
                .collect(Collectors.toList());
    }

    private List<List<String>> getColumnNames(Collection<Result> inputResults) {
        return inputResults.stream()
                .map(Result::getColumnNames)
                .collect(Collectors.toList());
    }
}
