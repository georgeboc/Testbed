package interactors;

import boundary.deserializers.OperationsDeserializer;
import entities.operations.deserialized.DeserializedOperations;
import entities.operations.logical.LogicalPlan;
import entities.operations.physical.PhysicalPlan;
import interactors.converters.deserializedToLogical.DeserializedToLogicalOperationsConverter;
import interactors.converters.logicalToPhysical.LogicalToPhysicalOperationsConverter;
import interactors.executors.Executor;
import lombok.RequiredArgsConstructor;

import java.util.logging.Logger;

@RequiredArgsConstructor
public class SparkRunnerInteractor implements Interactor {
    private final static Logger LOG = Logger.getLogger(SparkRunnerInteractor.class.getName());
    private final String pipelineFileName;
    private final OperationsDeserializer operationsDeserializer;
    private final DeserializedToLogicalOperationsConverter deserializedToLogicalOperationsConverter;
    private final LogicalToPhysicalOperationsConverter logicalToPhysicalOperationsConverter;
    private final Executor executor;

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
    }
}
