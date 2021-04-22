package com.testbed.interactors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.testbed.boundary.invocations.instrumentation.OperationInstrumentation;
import com.testbed.entities.invocations.InvocationPlan;
import com.testbed.entities.parameters.InputParameters;
import com.testbed.entities.parameters.OutputParameters;
import com.testbed.interactors.invokers.InvokerManager;
import com.testbed.interactors.viewers.InstrumentatedInvocationsViewer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RequiredArgsConstructor
public class InstrumentedInvocationsInteractor implements Interactor {
    private static final Logger LOG = LoggerFactory.getLogger(InstrumentedInvocationsInteractor.class.getName());

    private final InteractorCommons interactorCommons;
    private final InvokerManager invokerManager;
    private final List<OperationInstrumentation> operationInstrumentations;
    private final InstrumentatedInvocationsViewer instrumentatedInvocationsViewer;
    private final ObjectMapper objectMapper;

    @Override
    public void execute(final InputParameters inputParameters) {
        InvocationPlan invocationPlan = interactorCommons.createInvocationPlan(inputParameters);
        LOG.info("Invoking Invocation Plan");
        invokerManager.invoke(invocationPlan, inputParameters.getTolerableErrorPercentage());
        LOG.info("Operation Instrumentations after invocations: {}", operationInstrumentations);
        LOG.info("Creating output parameters for viewer");
        OutputParameters outputParameters = objectMapper.convertValue(inputParameters, OutputParameters.class);
        LOG.info("Viewing Operation Instrumentations to {}", inputParameters.getOutputPath());
        instrumentatedInvocationsViewer.view(outputParameters, operationInstrumentations);
    }
}
