package com.testbed.boundary.executors;

import com.testbed.entities.instrumentation.OperationInstrumentation;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class InstrumentedExecutable implements Executable {
    private final static String EXECUTABLE = "Executable";
    private final static String EMPTY = "";

    private final Executable wrappedExecutable;
    private final List<OperationInstrumentation> operationInstrumentations;

    @Override
    public Result execute(OperationInput operationInput) {
        Instant instantBeforeExecution = Instant.now();
        Result result = wrappedExecutable.execute(operationInput);
        Instant instantAfterExecution = Instant.now();
        Duration executionDuration = Duration.between(instantBeforeExecution, instantAfterExecution);
        String operationName = wrappedExecutable.getClass().getSimpleName().replace(EXECUTABLE, EMPTY);
        List<Long> inputRowsCounts = getInputRowsCounts(operationInput);
        long outputRowsCount = result.count();
        operationInstrumentations.add(OperationInstrumentation.builder()
                .instantBeforeExecution(instantBeforeExecution)
                .instantAfterExecution(instantAfterExecution)
                .executionDuration(executionDuration)
                .operationName(operationName)
                .inputRowsCount(inputRowsCounts)
                .outputRowsCount(outputRowsCount)
                .build());
        return result;
    }

    private List<Long> getInputRowsCounts(OperationInput operationInput) {
        return operationInput.getInputResults().stream()
                .map(Result::count)
                .collect(Collectors.toList());
    }
}
