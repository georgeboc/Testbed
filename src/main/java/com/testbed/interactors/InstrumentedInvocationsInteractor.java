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

    private final Deserializer<DeserializedOperations> operationsDeserializer;
    private final NotNullOnAllFieldsValidatorManager notNullOnAllFieldsValidatorManager;
    private final DeserializedToLogicalConverterManager deserializedToLogicalConverterManager;
    private final InputsCountValidatorManager inputsCountValidatorManager;
    private final LogicalToPhysicalConverterManager logicalToPhysicalConverterManager;

    private final InvocationPlanner invocationPlanner;
    private final InvokerManager invokerManager;

    private final List<OperationInstrumentation> operationInstrumentations;

    private final InvocationInstrumentationViewer invocationInstrumentationViewer;

    @Override
    public void execute(final Parameters parameters) {
        LOG.info("Deserializing operations from pipeline whose filename is: " + parameters.getPipelineFileName());
        DeserializedOperations deserializedOperations = operationsDeserializer.deserialize(parameters.getPipelineFileName());
        LOG.info("Deserialized pipeline: " + deserializedOperations);
        LOG.info("Validating if deserialized operations have values for all fields");
        notNullOnAllFieldsValidatorManager.validate(deserializedOperations);
        LOG.info("Deserialized operations are valid");
        LOG.info("Converting deserialized operations to logical operations");
        LogicalPlan logicalPlan = deserializedToLogicalConverterManager.convert(deserializedOperations);
        LOG.info("Logical Plan: " + logicalPlan);
        LOG.info("Validating if all operations have corresponding number of inputs in Logical Plan");
        inputsCountValidatorManager.validate(logicalPlan.getGraph());
        LOG.info("Logical Plan is valid");
        LOG.info("Converting logical operations to physical operations");
        PhysicalPlan physicalPlan = logicalToPhysicalConverterManager.convert(logicalPlan, parameters.getTolerableErrorPercentage());
        LOG.info("Physical Plan: " + physicalPlan);
        LOG.info("Creating Invocation Plan");
        InvocationPlan invocationPlan = invocationPlanner.createInvocationPlan(physicalPlan);
        LOG.info("Created Invocation Plan: " + invocationPlan);
        LOG.info("Invoking Invocation Plan");
        invokerManager.invoke(invocationPlan, parameters.getTolerableErrorPercentage());
        LOG.info("Operation Instrumentations after invocations: " + operationInstrumentations);
        LOG.info("Viewing Operation Instrumentations to " + parameters.getOutputPath());
        invocationInstrumentationViewer.view(parameters.getOutputPath(), operationInstrumentations);
    }
}
