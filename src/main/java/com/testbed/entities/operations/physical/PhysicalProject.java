package com.testbed.entities.operations.physical;

import lombok.Data;

import java.util.List;

@Data
public class PhysicalProject implements PhysicalOperation {
    private final List<String> projectedColumnNames;
    private final long expectedOutputColumnsCount;
}
