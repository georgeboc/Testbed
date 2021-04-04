package com.testbed.interactors.validators;

import com.google.common.collect.Streams;
import com.google.common.graph.Graph;
import com.testbed.entities.operations.logical.LogicalOperation;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@SuppressWarnings("UnstableApiUsage")
@RequiredArgsConstructor
public class InputsCountValidatorManager {
    public void validate(final Graph<LogicalOperation> logicalOperationGraph) {
        Stream<InputsCountValidator> validatorsStream = logicalOperationGraph.nodes().stream()
                .map(InputsCountValidatorDispatcher::dispatch);
        Streams.forEachPair(logicalOperationGraph.nodes().stream(),
                validatorsStream,
                (logicalOperation, inputsCountValidator) -> inputsCountValidator.validate(logicalOperation, logicalOperationGraph));
    }
}
