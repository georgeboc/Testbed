package factories;

import boundary.deserializers.OperationsDeserializer;
import boundary.readers.Reader;
import interactors.Interactor;
import interactors.SparkRunnerInteractor;
import interactors.converters.deserializedToLogical.DeserializedOperationsConverter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InteractorFactory {
    private final OperationsDeserializer pipelineDeserializer;
    private final DeserializedOperationsConverter deserializedOperationsConverter;
    private final Reader reader;

    public Interactor getReadJsonAndPrintContent(final String pipelineFileName) {
        return new SparkRunnerInteractor(pipelineFileName, pipelineDeserializer, deserializedOperationsConverter, reader);
    }
}
