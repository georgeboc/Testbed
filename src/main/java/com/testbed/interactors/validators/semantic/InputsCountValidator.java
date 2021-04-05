package com.testbed.interactors.validators.semantic;

import com.google.common.graph.Graph;
import com.testbed.entities.operations.logical.LogicalOperation;

@SuppressWarnings("UnstableApiUsage")
public interface InputsCountValidator {
    void validate(final LogicalOperation logicalOperation, final Graph<LogicalOperation> logicalOperationGraph);
}
