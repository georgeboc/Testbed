package com.testbed.entities.operations.physical;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PhysicalSelect implements PhysicalOperation {
    private final String _id;
    private final String lessThanValue;
    private final String columnName;
    private final long expectedOutputRowsCount;
}
