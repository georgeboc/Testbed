package com.testbed.interactors.validators.semantic;

import com.clearspring.analytics.util.Preconditions;
import com.google.common.graph.Graph;
import com.testbed.entities.operations.logical.LogicalOperation;

@SuppressWarnings("UnstableApiUsage")
public class UnaryInputsCountValidator implements InputsCountValidator {
    @Override
    public void validate(LogicalOperation logicalOperation, Graph<LogicalOperation> logicalOperationGraph) {
        int inputsCount = logicalOperationGraph.inDegree(logicalOperation);
        Preconditions.checkArgument(inputsCount == 1,
                "Unary operation %s is receiving %d inputs, although it is expected to receive one",
                logicalOperation.getClass().getSimpleName(), inputsCount);
    }
}
