package com.testbed.entities.operations.logical;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LogicalLoad implements LogicalOperation {
    private final String id;
    private final String datasetDirectoryPath;
}
