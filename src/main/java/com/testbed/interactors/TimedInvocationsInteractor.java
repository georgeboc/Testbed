package com.testbed.interactors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.testbed.entities.invocations.InvocationPlan;
import com.testbed.entities.parameters.InputParameters;
import com.testbed.entities.parameters.OutputParameters;
import com.testbed.interactors.invokers.InvokerManager;
import com.testbed.interactors.monitors.MonitoringInformation;
import com.testbed.interactors.viewers.TimedInvocationsViewer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
public class TimedInvocationsInteractor implements Interactor {
    private static final Logger LOG = LoggerFactory.getLogger(TimedInvocationsInteractor.class.getName());

    private final InteractorCommons interactorCommons;
    private final InvokerManager invokerManager;
    private final TimedInvocationsViewer timedInvocationsViewer;
    private final ObjectMapper objectMapper;

    @Override
    public void execute(final InputParameters inputParameters) {
        InvocationPlan invocationPlan = interactorCommons.createInvocationPlan(inputParameters);
        LOG.info("Invoking Invocation Plan");
        MonitoringInformation monitoringInformation = invokerManager.invoke(invocationPlan,
                inputParameters.getTolerableErrorPercentage());
        LOG.info("Collected monitoring information: {}", monitoringInformation);
        LOG.info("Creating output parameters for viewer");
        OutputParameters outputParameters = objectMapper.convertValue(inputParameters, OutputParameters.class);
        LOG.info("Viewing Invocation Time to {}", outputParameters.getOutputPath());
        timedInvocationsViewer.view(outputParameters,
                inputParameters.getFrameworkName(),
                monitoringInformation);
    }
}
