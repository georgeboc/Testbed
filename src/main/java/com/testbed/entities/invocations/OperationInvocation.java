package com.testbed.entities.invocations;

import com.testbed.entities.operations.physical.PhysicalOperation;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OperationInvocation {
    private final PhysicalOperation physicalOperation;
    private final int precedingPhysicalOperationsCount;
    private final int succeedingPhysicalOperationsCount;
    private final boolean isLastOperationBeforeSink;
}
