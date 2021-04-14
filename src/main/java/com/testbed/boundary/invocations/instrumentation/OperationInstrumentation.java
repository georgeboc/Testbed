package com.testbed.boundary.invocations.instrumentation;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OperationInstrumentation {
    private final String operationName;
    private final List<Long> inputsRowsCount;
    private final long outputRowsCount;
    private final List<List<String>> inputsColumnNames;
    private final List<String> outputColumnNames;
}
