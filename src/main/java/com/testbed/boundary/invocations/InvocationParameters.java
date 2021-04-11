package com.testbed.boundary.invocations;

import com.testbed.boundary.invocations.intermediateDatasets.IntermediateDataset;
import com.testbed.entities.operations.physical.PhysicalOperation;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class InvocationParameters {
    private final List<IntermediateDataset> inputIntermediateDatasets;
    private final PhysicalOperation physicalOperation;
    private final double tolerableErrorPercentage;
}

