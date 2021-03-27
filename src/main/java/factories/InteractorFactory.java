package factories;

import boundary.deserializers.OperationsDeserializer;
import interactors.Interactor;
import interactors.SparkRunnerInteractor;
import interactors.converters.deserializedToLogical.DeserializedOperationsConverter;
import interactors.converters.logicalToPhysical.LogicalOperationsConverter;
import interactors.executors.Executor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InteractorFactory {
    private final OperationsDeserializer pipelineDeserializer;
    private final DeserializedOperationsConverter deserializedOperationsConverter;
    private final LogicalOperationsConverter logicalOperationsConverter;
    private final Executor executor;

    public Interactor getReadJsonAndPrintContent(final String pipelineFileName) {
        return new SparkRunnerInteractor(pipelineFileName,
                pipelineDeserializer,
                deserializedOperationsConverter,
                logicalOperationsConverter,
                executor);
    }
}
