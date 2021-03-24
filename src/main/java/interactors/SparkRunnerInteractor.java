package interactors;

import boundary.deserializers.OperationsDeserializer;
import entities.operations.deserialized.DeserializedOperations;
import entities.operations.logical.LogicalPlan;
import interactors.converters.deserializedToLogical.DeserializedOperationsConverter;
import lombok.RequiredArgsConstructor;

import java.util.logging.Logger;

@RequiredArgsConstructor
public class SparkRunnerInteractor implements Interactor {
    private final static Logger LOG = Logger.getLogger(SparkRunnerInteractor.class.getName());
    private final String pipelineFileName;
    private final OperationsDeserializer operationsDeserializer;
    private final DeserializedOperationsConverter deserializedOperationsConverter;

    @Override
    public void execute() throws Exception {
        DeserializedOperations deserializedOperations = operationsDeserializer.deserialize(pipelineFileName);
        LOG.info("Deserialized pipeline: " + deserializedOperations);
        LogicalPlan logicalPlan = deserializedOperationsConverter.convert(deserializedOperations);
        LOG.info("Logical Plan: " + logicalPlan);
    }
}
