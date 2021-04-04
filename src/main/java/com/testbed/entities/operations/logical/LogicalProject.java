package com.testbed.entities.operations.logical;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LogicalProject implements UnaryLogicalOperation {
    private final String _id;
    private final double columnsSelectionFactor;
}
