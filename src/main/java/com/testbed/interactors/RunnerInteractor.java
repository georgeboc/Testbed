package com.testbed.interactors;

import com.testbed.boundary.deserializers.Deserializer;
import com.testbed.boundary.invocations.instrumentation.OperationInstrumentation;
import com.testbed.entities.invocations.InvocationPlan;
import com.testbed.entities.operations.deserialized.DeserializedOperations;
import com.testbed.entities.operations.logical.LogicalPlan;
import com.testbed.entities.operations.physical.PhysicalPlan;
import com.testbed.interactors.converters.deserializedToLogical.DeserializedToLogicalManager;
import com.testbed.interactors.converters.logicalToPhysical.LogicalToPhysicalManager;
import com.testbed.interactors.invokers.InvocationPlanner;
import com.testbed.interactors.invokers.InvokerManager;
import com.testbed.interactors.validators.semantic.InputsCountValidatorManager;
import com.testbed.interactors.validators.syntactic.NotNullOnAllFieldsValidatorManager;
import com.testbed.interactors.viewers.InvocationInstrumentationViewer;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.logging.Logger;

@RequiredArgsConstructor
public class RunnerInteractor implements Interactor {
    private static final Logger LOG = Logger.getLogger(RunnerInteractor.class.getName());

    private final String pipelineFileName;
    private final String operationInstrumentationsOutputPath;
    private final double tolerableErrorPercentage;

    private final Deserializer<DeserializedOperations> operationsDeserializer;
    private final NotNullOnAllFieldsValidatorManager notNullOnAllFieldsValidatorManager;
    private final DeserializedToLogicalManager deserializedToLogicalManager;
    private final InputsCountValidatorManager inputsCountValidatorManager;
    private final LogicalToPhysicalManager logicalToPhysicalManager;

    private final InvocationPlanner invocationPlanner;
    private final InvokerManager invokerManager;

    private final List<OperationInstrumentation> operationInstrumentations;

    private final InvocationInstrumentationViewer invocationInstrumentationViewer;

    @Override
    public void execute() {
        LOG.info("Deserializing operations from pipeline whose filename is: " + pipelineFileName);
        DeserializedOperations deserializedOperations = operationsDeserializer.deserialize(pipelineFileName);
        LOG.info("Deserialized pipeline: " + deserializedOperations);
        LOG.info("Validating if deserialized operations have values for all fields");
        notNullOnAllFieldsValidatorManager.validate(deserializedOperations);
        LOG.info("Deserialized operations are valid");
        LOG.info("Converting deserialized operations to logical operations");
        LogicalPlan logicalPlan = deserializedToLogicalManager.convert(deserializedOperations);
        LOG.info("Logical Plan: " + logicalPlan);
        LOG.info("Validating if all operations have corresponding number of inputs in Logical Plan");
        inputsCountValidatorManager.validate(logicalPlan.getGraph());
        LOG.info("Logical Plan is valid");
        LOG.info("Converting logical operations to physical operations");
        PhysicalPlan physicalPlan = logicalToPhysicalManager.convert(logicalPlan, tolerableErrorPercentage);
        LOG.info("Physical Plan: " + physicalPlan);
        LOG.info("Creating Invocation Plan");
        InvocationPlan invocationPlan = invocationPlanner.createInvocationPlan(physicalPlan);
        LOG.info("Created Invocation Plan: " + invocationPlan);
        LOG.info("Invoking Invocation Plan");
        invokerManager.invoke(invocationPlan, tolerableErrorPercentage);
        LOG.info("Operation Instrumentations after invocations: " + operationInstrumentations);
        LOG.info("Viewing Operation Instrumentations to " + operationInstrumentationsOutputPath);
        invocationInstrumentationViewer.view(operationInstrumentationsOutputPath, operationInstrumentations);
    }
}
