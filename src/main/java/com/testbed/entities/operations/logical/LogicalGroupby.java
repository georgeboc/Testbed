package com.testbed.entities.operations.logical;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class LogicalGroupby implements LogicalOperation {
    private final String id;
    private final List<String> groupingColumnNames;
}
