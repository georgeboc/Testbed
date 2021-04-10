package com.testbed.entities.operations.physical;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PhysicalGroupby implements PhysicalOperation {
    private final String id;
    private final List<String> groupingColumnNames;
}
