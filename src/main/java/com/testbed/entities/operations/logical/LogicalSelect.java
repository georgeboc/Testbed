package com.testbed.entities.operations.logical;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LogicalSelect implements UnaryLogicalOperation {
    private final String _id;
    private final double selectivityFactor;
    private final String columnName;
}
