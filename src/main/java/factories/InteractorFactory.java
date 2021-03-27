package factories;

import boundary.deserializers.OperationsDeserializer;
import interactors.Interactor;
import interactors.SparkRunnerInteractor;
import interactors.converters.deserializedToLogical.DeserializedToLogicalOperationsConverter;
import interactors.converters.logicalToPhysical.LogicalToPhysicalOperationsConverter;
import interactors.executors.Executor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InteractorFactory {
    private final OperationsDeserializer pipelineDeserializer;
    private final DeserializedToLogicalOperationsConverter deserializedToLogicalOperationsConverter;
    private final LogicalToPhysicalOperationsConverter logicalToPhysicalOperationsConverter;
    private final Executor executor;

    public Interactor getReadJsonAndPrintContent(final String pipelineFileName) {
        return new SparkRunnerInteractor(pipelineFileName,
                pipelineDeserializer,
                deserializedToLogicalOperationsConverter,
                logicalToPhysicalOperationsConverter,
                executor);
    }
}
