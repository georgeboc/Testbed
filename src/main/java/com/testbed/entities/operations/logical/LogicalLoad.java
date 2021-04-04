package com.testbed.entities.operations.logical;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LogicalLoad implements ZeroaryLogicalOperation {
    private final String _id;
    private final String datasetDirectoryPath;
}
