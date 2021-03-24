package factories;

import boundary.deserializers.OperationsDeserializer;
import interactors.Interactor;
import interactors.SparkRunnerInteractor;
import interactors.converters.deserializedToLogical.DeserializedOperationsConverter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InteractorFactory {
    private final OperationsDeserializer pipelineDeserializer;
    private final DeserializedOperationsConverter deserializedOperationsConverter;

    public Interactor getReadJsonAndPrintContent(final String pipelineFileName) {
        return new SparkRunnerInteractor(pipelineFileName,
                pipelineDeserializer,
                deserializedOperationsConverter);
    }
}
