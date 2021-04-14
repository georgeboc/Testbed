package com.testbed.interactors;

import com.testbed.boundary.deserializers.Deserializer;
import com.testbed.boundary.invocations.instrumentation.OperationInstrumentation;
import com.testbed.entities.operations.deserialized.DeserializedOperations;
import com.testbed.interactors.converters.deserializedToLogical.DeserializedToLogicalManager;
import com.testbed.interactors.converters.logicalToPhysical.LogicalToPhysicalManager;
import com.testbed.interactors.invokers.InvocationPlanner;
import com.testbed.interactors.invokers.InvokerManager;
import com.testbed.interactors.validators.semantic.InputsCountValidatorManager;
import com.testbed.interactors.validators.syntactic.NotNullOnAllFieldsValidatorManager;
import com.testbed.interactors.viewers.InvocationInstrumentationViewer;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class InteractorFactory {
    private final Deserializer<DeserializedOperations> operationsDeserializer;
    private final NotNullOnAllFieldsValidatorManager notNullOnAllFieldsValidatorManager;
    private final DeserializedToLogicalManager deserializedToLogicalManager;
    private final InputsCountValidatorManager inputsCountValidatorManager;
    private final LogicalToPhysicalManager logicalToPhysicalManager;
    private final InvocationPlanner invocationPlanner;
    private final InvokerManager invokerManager;
    private final List<OperationInstrumentation> operationInstrumentations;
    private final InvocationInstrumentationViewer invocationInstrumentationViewer;

    public Interactor getReadJsonAndPrintContent(final String pipelineFileName,
                                                 final String operationInstrumentationsOutputPath,
                                                 final double tolerableErrorPercentage) {
        return new RunnerInteractor(pipelineFileName,
                operationInstrumentationsOutputPath,
                tolerableErrorPercentage,
                operationsDeserializer,
                notNullOnAllFieldsValidatorManager,
                deserializedToLogicalManager,
                inputsCountValidatorManager,
                logicalToPhysicalManager,
                invocationPlanner,
                invokerManager,
                operationInstrumentations,
                invocationInstrumentationViewer);
    }
}
