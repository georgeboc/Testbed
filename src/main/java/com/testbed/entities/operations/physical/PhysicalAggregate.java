package com.testbed.entities.operations.physical;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PhysicalAggregate implements PhysicalOperation {
    private final String id;
    private String aggregationColumnName;
    private String aggregationOperation;
}
