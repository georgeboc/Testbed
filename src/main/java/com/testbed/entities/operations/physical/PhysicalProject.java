package com.testbed.entities.operations.physical;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PhysicalProject implements PhysicalOperation {
    private final String _id;
    private final List<String> projectedColumnNames;
    private final double expectedColumnsSelectionFactor;
}
