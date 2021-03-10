package interactors;

import boundary.deserializers.OperationsDeserializer;
import boundary.readers.Reader;
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
    private final Reader reader;

    @Override
    public void execute() throws Exception {
        String pipelineString = reader.read(pipelineFileName);
        LOG.info("Read from JSON file: " + pipelineString);
        DeserializedOperations deserializedOperations = operationsDeserializer.deserialize(pipelineString);
        LOG.info("Deserialized pipeline: " + deserializedOperations);
        LogicalPlan logicalPlan = deserializedOperationsConverter.convert(deserializedOperations);
        LOG.info("Logical Plan: " + logicalPlan);
    }
}
