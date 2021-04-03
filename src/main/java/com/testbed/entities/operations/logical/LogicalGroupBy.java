package com.testbed.entities.operations.logical;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LogicalGroupBy implements LogicalOperation {
    private final String _id;
    private final String columnName;
}
