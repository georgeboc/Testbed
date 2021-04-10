package com.testbed.boundary.invocations;

import com.google.common.base.CaseFormat;
import com.testbed.boundary.invocations.results.Result;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class InstrumentInvokable implements Invokable {
    private final Invokable wrappedInvokable;
    private final List<OperationInstrumentation> operationInstrumentations;

    @Override
    public Result invoke(final InvocationParameters invocationParameters) {
        Result result = wrappedInvokable.invoke(invocationParameters);
        String className = wrappedInvokable.getClass().getSimpleName();
        String[] classNameParts = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, className).split("_");
        String operationName = StringUtils.capitalize(Arrays.stream(classNameParts).findFirst().get());
        List<Long> inputsRowsCounts = getRowsCounts(invocationParameters.getInputResults());
        long outputRowsCount = result.count();
        List<List<String>> inputsColumnNames = getColumnNames(invocationParameters.getInputResults());
        List<String> outputColumnNames = result.getColumnNames();
        operationInstrumentations.add(OperationInstrumentation.builder()
                .operationName(operationName)
                .inputsRowsCount(inputsRowsCounts)
                .outputRowsCount(outputRowsCount)
                .inputsColumnNames(inputsColumnNames)
                .outputColumnNames(outputColumnNames)
                .build());
        return result;
    }

    private List<Long> getRowsCounts(final Collection<Result> inputResults) {
        return inputResults.stream()
                .map(Result::count)
                .collect(Collectors.toList());
    }

    private List<List<String>> getColumnNames(final Collection<Result> inputResults) {
        return inputResults.stream()
                .map(Result::getColumnNames)
                .collect(Collectors.toList());
    }
}
