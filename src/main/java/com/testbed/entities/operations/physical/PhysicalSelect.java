package com.testbed.entities.operations.physical;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PhysicalSelect implements PhysicalOperation {
    private final String id;
    private final String lessThanOrEqualValue;
    private final String columnName;
}
