package com.testbed.interactors;

import com.testbed.boundary.deserializers.Deserializer;
import com.testbed.boundary.invocations.instrumentation.OperationInstrumentation;
import com.testbed.entities.invocations.InvocationPlan;
import com.testbed.entities.operations.deserialized.DeserializedOperations;
import com.testbed.entities.operations.logical.LogicalPlan;
import com.testbed.entities.operations.physical.PhysicalPlan;
import com.testbed.entities.parameters.Parameters;
import com.testbed.interactors.converters.deserializedToLogical.DeserializedToLogicalConverterManager;
import com.testbed.interactors.converters.logicalToPhysical.LogicalToPhysicalConverterManager;
import com.testbed.interactors.invokers.InvocationPlanner;
import com.testbed.interactors.invokers.InvokerManager;
import com.testbed.interactors.validators.semantic.InputsCountValidatorManager;
import com.testbed.interactors.validators.syntactic.NotNullOnAllFieldsValidatorManager;
import com.testbed.interactors.viewers.InvocationInstrumentationViewer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RequiredArgsConstructor
public class InstrumentedInvocationsInteractor implements Interactor {
    private static final Logger LOG = LoggerFactory.getLogger(InstrumentedInvocationsInteractor.class.getName());

    private final InteractorCommons interactorCommons;
    private final InvokerManager invokerManager;
    private final List<OperationInstrumentation> operationInstrumentations;
    private final InvocationInstrumentationViewer invocationInstrumentationViewer;

    @Override
    public void execute(final Parameters parameters) {
        InvocationPlan invocationPlan = interactorCommons.createInvocationPlan(parameters);
        LOG.info("Invoking Invocation Plan");
        invokerManager.invoke(invocationPlan, parameters.getTolerableErrorPercentage());
        LOG.info("Operation Instrumentations after invocations: " + operationInstrumentations);
        LOG.info("Viewing Operation Instrumentations to " + parameters.getOutputPath());
        invocationInstrumentationViewer.view(parameters.getOutputPath(), operationInstrumentations);
    }
}
