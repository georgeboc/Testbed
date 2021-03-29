package com.testbed.interactors;

import com.testbed.boundary.deserializers.Deserializer;
import com.testbed.boundary.serializers.Serializer;
import com.testbed.entities.instrumentation.OperationInstrumentation;
import com.testbed.entities.operations.deserialized.DeserializedOperations;
import com.testbed.entities.operations.logical.LogicalPlan;
import com.testbed.entities.operations.physical.PhysicalPlan;
import com.testbed.interactors.converters.deserializedToLogical.DeserializedToLogicalOperationsConverter;
import com.testbed.interactors.converters.logicalToPhysical.LogicalToPhysicalOperationsConverter;
import com.testbed.interactors.executors.Executor;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.logging.Logger;

@RequiredArgsConstructor
public class SparkRunnerInteractor implements Interactor {
    private final static Logger LOG = Logger.getLogger(SparkRunnerInteractor.class.getName());
    private final String pipelineFileName;
    private final String callInstrumentationsOutputPath;
    private final Deserializer<DeserializedOperations> operationsDeserializer;
    private final DeserializedToLogicalOperationsConverter deserializedToLogicalOperationsConverter;
    private final LogicalToPhysicalOperationsConverter logicalToPhysicalOperationsConverter;
    private final Executor executor;
    private final List<OperationInstrumentation> operationInstrumentations;
    private final Serializer<List<OperationInstrumentation>> callInstrumentationsSerializer;

    @Override
    public void execute() throws Exception {
        LOG.info("Deserializing operations from pipeline whose filename is: " + pipelineFileName);
        DeserializedOperations deserializedOperations = operationsDeserializer.deserialize(pipelineFileName);
        LOG.info("Deserialized pipeline: " + deserializedOperations);
        LOG.info("Converting deserialized operations to logical operations");
        LogicalPlan logicalPlan = deserializedToLogicalOperationsConverter.convert(deserializedOperations);
        LOG.info("Logical Plan: " + logicalPlan);
        LOG.info("Converting logical operations to physical operations");
        PhysicalPlan physicalPlan = logicalToPhysicalOperationsConverter.convert(logicalPlan);
        LOG.info("Physical Plan: " + physicalPlan);
        LOG.info("Executing Physical Plan");
        executor.execute(physicalPlan);
        LOG.info("Call Instrumentations after execution: " + operationInstrumentations);
        LOG.info("Serializing Call Instrumentations to " + callInstrumentationsOutputPath);
        callInstrumentationsSerializer.serialize(callInstrumentationsOutputPath, operationInstrumentations);
    }
}
