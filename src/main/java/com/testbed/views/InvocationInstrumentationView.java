package com.testbed.views;

import lombok.Data;

import java.util.List;

@Data
public class InvocationInstrumentationView {
    public long invocationOrder;
    public String operationName;
    public List<Long> inputsRowsCount;
    public long outputRowsCount;
    public List<List<String>> inputsColumnNames;
    public List<String> outputColumnNames;
}
