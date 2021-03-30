package com.testbed.entities.instrumentation;

import lombok.Builder;
import lombok.Data;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Data
@Builder
public class OperationInstrumentation {
    private final String operationName;
    private final List<Long> inputsRowsCount;
    private final long outputRowsCount;
    private final List<List<String>> inputsColumnNames;
    private final List<String> outputColumnNames;
    private final Instant instantBeforeExecution;
    private final Instant instantAfterExecution;
    private final Duration executionDuration;
}
