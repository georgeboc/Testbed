package com.testbed.entities.operations.logical;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LogicalAggregate implements UnaryLogicalOperation {
    private final String id;
    private String aggregationColumnName;
    private String aggregationOperation;
}
