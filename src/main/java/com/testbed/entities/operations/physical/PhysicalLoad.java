package com.testbed.entities.operations.physical;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PhysicalLoad implements PhysicalOperation {
    private final String _id;
    private final String datasetDirectoryPath;
}
