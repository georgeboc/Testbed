package com.testbed.interactors.validators.semantic;

import com.google.common.collect.Streams;
import com.google.common.graph.Graph;
import com.testbed.entities.operations.logical.LogicalOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils;
import org.springframework.context.ApplicationContext;

import javax.inject.Inject;
import java.util.stream.Stream;

@SuppressWarnings("UnstableApiUsage")
@RequiredArgsConstructor
public class InputsCountValidatorManager {
    @Inject
    private ApplicationContext applicationContext;

    public void validate(final Graph<LogicalOperation> logicalOperationGraph) {
        Stream<InputsCountValidator> validatorsStream = logicalOperationGraph.nodes().stream()
                .map(this::getInputsCountValidator);
        Streams.forEachPair(logicalOperationGraph.nodes().stream(),
                validatorsStream,
                (logicalOperation, inputsCountValidator) -> inputsCountValidator.validate(logicalOperation,
                        logicalOperationGraph));
    }

    private InputsCountValidator getInputsCountValidator(final LogicalOperation logicalOperation) {
        return BeanFactoryAnnotationUtils.qualifiedBeanOfType(applicationContext.getAutowireCapableBeanFactory(),
                InputsCountValidator.class, logicalOperation.getClass().getSimpleName());
    }
}
