package com.testbed.entities.operations.physical;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PhysicalGroupBy implements PhysicalOperation {
    private final String _id;
    private final String columnName;
}
