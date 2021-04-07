package com.testbed.entities.invocations;

import com.testbed.entities.operations.physical.PhysicalOperation;
import lombok.Data;

@Data
public class OperationInvocation {
    private final PhysicalOperation physicalOperation;
    private final int precedingPhysicalOperationsCount;
    private final int succeedingPhysicalOperationsCount;
}
