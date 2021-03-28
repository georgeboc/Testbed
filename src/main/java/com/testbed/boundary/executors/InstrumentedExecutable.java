package com.testbed.boundary.executors;

import com.testbed.entities.instrumentation.CallInstrumentation;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class InstrumentedExecutable implements Executable {
    private final Executable wrappedExecutable;
    private final List<CallInstrumentation> callInstrumentations;

    @Override
    public Result execute(OperationInput operationInput) {
        Instant instantBeforeExecution = Instant.now();
        Result result = wrappedExecutable.execute(operationInput);
        Instant instantAfterExecution = Instant.now();
        Duration executionDuration = Duration.between(instantBeforeExecution, instantAfterExecution);
        String className = wrappedExecutable.getClass().getSimpleName();
        List<Long> inputRowsCounts = getInputRowsCounts(operationInput);
        long outputRowsCount = result.count();
        callInstrumentations.add(CallInstrumentation.builder()
                .instantBeforeExecution(instantBeforeExecution)
                .instantAfterExecution(instantAfterExecution)
                .executionDuration(executionDuration)
                .className(className)
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
