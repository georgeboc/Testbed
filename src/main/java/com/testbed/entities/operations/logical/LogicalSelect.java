package com.testbed.entities.operations.logical;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LogicalSelect implements LogicalOperation {
    private final String id;
    private final double approximatedRowsSelectivityFactor;
    private final String columnName;
}
