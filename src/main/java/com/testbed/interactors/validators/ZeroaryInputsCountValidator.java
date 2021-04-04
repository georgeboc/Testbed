package com.testbed.interactors.validators;

import com.clearspring.analytics.util.Preconditions;
import com.google.common.graph.Graph;
import com.testbed.entities.operations.logical.LogicalOperation;

@SuppressWarnings("UnstableApiUsage")
public class ZeroaryInputsCountValidator implements InputsCountValidator {
    @Override
    public void validate(LogicalOperation logicalOperation, Graph<LogicalOperation> logicalOperationGraph) {
        int inputsCount = logicalOperationGraph.inDegree(logicalOperation);
        Preconditions.checkArgument(inputsCount == 0,
                "Zero-ary operation %s is receiving %d inputs, although it is expected to receive none",
                logicalOperation.getClass().getSimpleName(), inputsCount);
    }
}
