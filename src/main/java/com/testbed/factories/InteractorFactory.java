package com.testbed.factories;

import com.testbed.boundary.deserializers.OperationsDeserializer;
import com.testbed.entities.instrumentation.CallInstrumentation;
import com.testbed.interactors.Interactor;
import com.testbed.interactors.SparkRunnerInteractor;
import com.testbed.interactors.converters.deserializedToLogical.DeserializedToLogicalOperationsConverter;
import com.testbed.interactors.converters.logicalToPhysical.LogicalToPhysicalOperationsConverter;
import com.testbed.interactors.executors.Executor;
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
