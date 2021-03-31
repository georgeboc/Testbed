package com.testbed.views;

import lombok.Data;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Data
public class InvocationInstrumentationView {
    public long invocationOrder;
    public String operationName;
    public List<Long> inputsRowsCount;
    public long outputRowsCount;
    public List<List<String>> inputsColumnNames;
    public List<String> outputColumnNames;
    public Instant instantBeforeInvocation;
    public Instant instantAfterInvocation;
    public Duration invocationDuration;
}
