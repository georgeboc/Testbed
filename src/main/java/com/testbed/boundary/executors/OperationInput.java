package com.testbed.boundary.executors;

import com.testbed.entities.operations.physical.PhysicalOperation;
import lombok.Builder;
import lombok.Data;

import java.util.Collection;

@Data
@Builder
public class OperationInput {
    private final Collection<Result> inputResults;
    private final PhysicalOperation physicalOperation;
}

