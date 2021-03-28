package com.testbed.boundary.executors;

import com.testbed.entities.instrumentation.CallInstrumentation;
import lombok.RequiredArgsConstructor;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class InstrumentedExecutable implements Executable {
    private static final String SPARK_RESULT = "SparkResult";
    private static final String INTEGER_RESULT = "IntegerResult";
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
        long outputRowsCount = getOutputRowsCount(result);
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
                .filter(inputResult -> inputResult.getClass().getSimpleName().equals(SPARK_RESULT))
                .map(inputResult -> (Dataset<Row>) inputResult.getValue())
                .map(Dataset::count)
                .collect(Collectors.toList());
    }

    private long getOutputRowsCount(Result result) {
        long outputRowsCount = 1;
        if (result.getClass().getSimpleName().equals(SPARK_RESULT)) {
            outputRowsCount = ((Dataset<Row>) result.getValue()).count();
        }
        return outputRowsCount;
    }
}
