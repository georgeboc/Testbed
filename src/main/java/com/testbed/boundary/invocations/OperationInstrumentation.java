package com.testbed.boundary.invocations;

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
    private final Instant instantBeforeInvocation;
    private final Instant instantAfterInvocation;
    private final Duration invocationDuration;
}
