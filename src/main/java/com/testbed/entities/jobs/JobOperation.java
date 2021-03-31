package com.testbed.entities.jobs;

import com.testbed.entities.operations.physical.PhysicalOperation;
import lombok.Data;

@Data
public class JobOperation {
    private final PhysicalOperation physicalOperation;
    private final int precedingPhysicalOperationsCount;
    private final int succeedingPhysicalOperationsCount;
}
