package com.testbed.entities.operations.logical;

import lombok.Data;

@Data
public class LogicalProject implements LogicalOperation {
    private final double columnsSelectionFactor;
}
