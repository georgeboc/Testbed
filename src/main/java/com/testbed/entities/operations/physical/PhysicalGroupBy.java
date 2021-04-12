package com.testbed.entities.operations.physical;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PhysicalGroupBy implements PhysicalOperation {
    private final String id;
    private final List<String> groupingColumnNames;
    private final List<String> originalColumnNames;
}
