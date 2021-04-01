package com.testbed.factories;

import com.testbed.boundary.deserializers.Deserializer;
import com.testbed.boundary.invocations.OperationInstrumentation;
import com.testbed.entities.operations.deserialized.DeserializedOperations;
import com.testbed.interactors.Interactor;
import com.testbed.interactors.SparkRunnerInteractor;
import com.testbed.interactors.converters.deserializedToLogical.DeserializedToLogicalOperationsConverter;
import com.testbed.interactors.converters.logicalToPhysical.LogicalToPhysicalOperationsConverter;
import com.testbed.interactors.jobs.JobCreator;
import com.testbed.interactors.jobs.JobInvoker;
import com.testbed.interactors.viewers.InvocationInstrumentationViewer;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class InteractorFactory {
    private final Deserializer<DeserializedOperations> operationsDeserializer;
    private final DeserializedToLogicalOperationsConverter deserializedToLogicalOperationsConverter;
    private final LogicalToPhysicalOperationsConverter logicalToPhysicalOperationsConverter;
    private final JobCreator jobCreator;
    private final JobInvoker jobInvoker;
    private final List<OperationInstrumentation> operationInstrumentations;
    private final InvocationInstrumentationViewer invocationInstrumentationViewer;

    public Interactor getReadJsonAndPrintContent(final String pipelineFileName,
                                                 final String operationInstrumentationsOutputPath,
                                                 final double tolerableErrorPercentage) {
        return new SparkRunnerInteractor(pipelineFileName,
                operationInstrumentationsOutputPath,
                tolerableErrorPercentage,
                operationsDeserializer,
                deserializedToLogicalOperationsConverter,
                logicalToPhysicalOperationsConverter,
                jobCreator,
                jobInvoker,
                operationInstrumentations,
                invocationInstrumentationViewer);
    }
}
