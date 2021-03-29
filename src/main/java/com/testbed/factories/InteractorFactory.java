package com.testbed.factories;

import com.testbed.boundary.deserializers.Deserializer;
import com.testbed.boundary.serializers.Serializer;
import com.testbed.entities.instrumentation.OperationInstrumentation;
import com.testbed.entities.operations.deserialized.DeserializedOperations;
import com.testbed.interactors.Interactor;
import com.testbed.interactors.SparkRunnerInteractor;
import com.testbed.interactors.converters.deserializedToLogical.DeserializedToLogicalOperationsConverter;
import com.testbed.interactors.converters.logicalToPhysical.LogicalToPhysicalOperationsConverter;
import com.testbed.interactors.executors.Executor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class InteractorFactory {
    private final Deserializer<DeserializedOperations> operationsDeserializer;
    private final DeserializedToLogicalOperationsConverter deserializedToLogicalOperationsConverter;
    private final LogicalToPhysicalOperationsConverter logicalToPhysicalOperationsConverter;
    private final Executor executor;
    private final List<OperationInstrumentation> operationInstrumentations;
    private final Serializer<List<OperationInstrumentation>> operationInstrumentationsSerializer;

    public Interactor getReadJsonAndPrintContent(final String pipelineFileName,
                                                 final String operationInstrumentationsOutputPath) {
        return new SparkRunnerInteractor(pipelineFileName,
                operationInstrumentationsOutputPath,
                operationsDeserializer,
                deserializedToLogicalOperationsConverter,
                logicalToPhysicalOperationsConverter,
                executor,
                operationInstrumentations,
                operationInstrumentationsSerializer);
    }
}
