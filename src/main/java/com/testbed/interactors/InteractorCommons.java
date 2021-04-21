package com.testbed.interactors;

import com.testbed.boundary.deserializers.Deserializer;
import com.testbed.entities.invocations.InvocationPlan;
import com.testbed.entities.operations.deserialized.DeserializedOperations;
import com.testbed.entities.operations.logical.LogicalPlan;
import com.testbed.entities.operations.physical.PhysicalPlan;
import com.testbed.entities.parameters.Parameters;
import com.testbed.interactors.converters.deserializedToLogical.DeserializedToLogicalConverterManager;
import com.testbed.interactors.converters.logicalToPhysical.LogicalToPhysicalConverterManager;
import com.testbed.interactors.invokers.InvocationPlanner;
import com.testbed.interactors.validators.semantic.InputsCountValidatorManager;
import com.testbed.interactors.validators.syntactic.NotNullOnAllFieldsValidatorManager;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
public class InteractorCommons {
    private static final Logger LOG = LoggerFactory.getLogger(InteractorCommons.class.getName());

    private final Deserializer<DeserializedOperations> operationsDeserializer;
    private final NotNullOnAllFieldsValidatorManager notNullOnAllFieldsValidatorManager;
    private final DeserializedToLogicalConverterManager deserializedToLogicalConverterManager;
    private final InputsCountValidatorManager inputsCountValidatorManager;
    private final LogicalToPhysicalConverterManager logicalToPhysicalConverterManager;

    private final InvocationPlanner invocationPlanner;

    public InvocationPlan createInvocationPlan(final Parameters parameters) {
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
        return invocationPlan;
    }
}
