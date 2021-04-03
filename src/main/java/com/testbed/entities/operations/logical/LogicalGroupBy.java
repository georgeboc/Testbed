package com.testbed.entities.operations.logical;

import lombok.Data;

@Data
public class LogicalGroupBy implements LogicalOperation {
    private final String _id;
    private final String operation;
    private final String columnName;
}
