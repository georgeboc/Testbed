package com.testbed.entities.operations.physical;

import lombok.Data;

@Data
public class PhysicalGroupBy implements PhysicalOperation {
    private final String _id;
    private final String operation;
    private final String columnName;
}
