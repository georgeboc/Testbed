package factories;

import boundary.deserializers.OperationsDeserializer;
import entities.instrumentation.CallInstrumentation;
import interactors.Interactor;
import interactors.SparkRunnerInteractor;
import interactors.converters.deserializedToLogical.DeserializedToLogicalOperationsConverter;
import interactors.converters.logicalToPhysical.LogicalToPhysicalOperationsConverter;
import interactors.executors.Executor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class InteractorFactory {
    private final OperationsDeserializer pipelineDeserializer;
    private final DeserializedToLogicalOperationsConverter deserializedToLogicalOperationsConverter;
    private final LogicalToPhysicalOperationsConverter logicalToPhysicalOperationsConverter;
    private final Executor executor;
    private final List<CallInstrumentation> callInstrumentations;

    public Interactor getReadJsonAndPrintContent(final String pipelineFileName) {
        return new SparkRunnerInteractor(pipelineFileName,
                pipelineDeserializer,
                deserializedToLogicalOperationsConverter,
                logicalToPhysicalOperationsConverter,
                executor,
                callInstrumentations);
    }
}
