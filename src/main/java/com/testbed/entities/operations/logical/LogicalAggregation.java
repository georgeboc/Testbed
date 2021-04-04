package com.testbed.entities.operations.logical;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LogicalAggregation implements LogicalOperation {
    private final String _id;
    private String aggregationColumnName;
    private String operation;
}
