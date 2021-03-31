package com.testbed.interactors;

import com.testbed.boundary.deserializers.Deserializer;
import com.testbed.boundary.invocations.OperationInstrumentation;
import com.testbed.entities.jobs.Job;
import com.testbed.entities.operations.deserialized.DeserializedOperations;
import com.testbed.entities.operations.logical.LogicalPlan;
import com.testbed.entities.operations.physical.PhysicalPlan;
import com.testbed.interactors.converters.deserializedToLogical.DeserializedToLogicalOperationsConverter;
import com.testbed.interactors.converters.logicalToPhysical.LogicalToPhysicalOperationsConverter;
import com.testbed.interactors.jobs.JobCreator;
import com.testbed.interactors.jobs.JobInvoker;
import com.testbed.interactors.viewers.InvocationInstrumentationViewer;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.logging.Logger;

@RequiredArgsConstructor
public class SparkRunnerInteractor implements Interactor {
    private final static Logger LOG = Logger.getLogger(SparkRunnerInteractor.class.getName());

    private final String pipelineFileName;
    private final String operationInstrumentationsOutputPath;

    private final Deserializer<DeserializedOperations> operationsDeserializer;
    private final DeserializedToLogicalOperationsConverter deserializedToLogicalOperationsConverter;
    private final LogicalToPhysicalOperationsConverter logicalToPhysicalOperationsConverter;

    private final JobCreator jobCreator;
    private final JobInvoker jobInvoker;

    private final List<OperationInstrumentation> operationInstrumentations;

    private final InvocationInstrumentationViewer invocationInstrumentationViewer;

    @Override
    public void execute() {
        LOG.info("Deserializing operations from pipeline whose filename is: " + pipelineFileName);
        DeserializedOperations deserializedOperations = operationsDeserializer.deserialize(pipelineFileName);
        LOG.info("Deserialized pipeline: " + deserializedOperations);
        LOG.info("Converting deserialized operations to logical operations");
        LogicalPlan logicalPlan = deserializedToLogicalOperationsConverter.convert(deserializedOperations);
        LOG.info("Logical Plan: " + logicalPlan);
        LOG.info("Converting logical operations to physical operations");
        PhysicalPlan physicalPlan = logicalToPhysicalOperationsConverter.convert(logicalPlan);
        LOG.info("Physical Plan: " + physicalPlan);
        LOG.info("Creating job");
        Job job = jobCreator.createJob(physicalPlan);
        LOG.info("Created job: " + job);
        LOG.info("Invoking job");
        jobInvoker.invokeJob(job);
        LOG.info("Operation Instrumentations after job invocation: " + operationInstrumentations);
        LOG.info("Viewing Operation Instrumentations to " + operationInstrumentationsOutputPath);
        invocationInstrumentationViewer.view(operationInstrumentationsOutputPath, operationInstrumentations);
    }
}
