package com.testbed.entities.invocations;

import com.testbed.entities.operations.physical.PhysicalOperation;
import lombok.Builder;
import lombok.Data;

import java.util.Collection;
import java.util.List;

@Data
@Builder
public class OperationInvocation {
    private final PhysicalOperation physicalOperation;
    private final Collection<PhysicalOperation> succeedingPhysicalOperations;
    private final boolean isLastOperationBeforeSink;
}
