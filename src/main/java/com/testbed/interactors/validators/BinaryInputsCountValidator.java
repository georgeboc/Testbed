package com.testbed.interactors.validators;

import com.clearspring.analytics.util.Preconditions;
import com.google.common.graph.Graph;
import com.testbed.entities.operations.logical.LogicalOperation;
import lombok.RequiredArgsConstructor;

@SuppressWarnings("UnstableApiUsage")
@RequiredArgsConstructor
public class BinaryInputsCountValidator implements InputsCountValidator {
    @Override
    public void validate(LogicalOperation logicalOperation, Graph<LogicalOperation> logicalOperationGraph) {
        int inputsCount = logicalOperationGraph.inDegree(logicalOperation);
        Preconditions.checkArgument(inputsCount == 2,
                "Binary operation %s is receiving %d inputs, although it is expected to receive two",
                logicalOperation.getClass().getSimpleName(), inputsCount);
    }
}
