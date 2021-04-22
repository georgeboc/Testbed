package com.testbed.interactors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Stopwatch;
import com.testbed.entities.invocations.InvocationPlan;
import com.testbed.entities.parameters.InputParameters;
import com.testbed.entities.parameters.OutputParameters;
import com.testbed.interactors.invokers.InvokerManager;
import com.testbed.interactors.viewers.TimedInvocationsViewer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

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
        Stopwatch stopwatchedInvocations = invokerManager.invoke(invocationPlan, inputParameters.getTolerableErrorPercentage());
        long durationInNanoseconds = stopwatchedInvocations.elapsed(TimeUnit.NANOSECONDS);
        LOG.info("Invocations finished in {} ns", durationInNanoseconds);
        LOG.info("Creating output parameters for viewer");
        OutputParameters outputParameters = objectMapper.convertValue(inputParameters, OutputParameters.class);
        LOG.info("Viewing Invocation Time to {}", outputParameters.getOutputPath());
        timedInvocationsViewer.view(outputParameters,
                inputParameters.getFrameworkConfiguration().getFrameworkName(),
                durationInNanoseconds);
    }
}
