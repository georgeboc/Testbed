package com.testbed.interactors;

import com.google.common.base.Stopwatch;
import com.testbed.entities.invocations.InvocationPlan;
import com.testbed.entities.parameters.Parameters;
import com.testbed.interactors.invokers.InvokerManager;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class TimedInvocationsInteractor implements Interactor {
    private static final Logger LOG = LoggerFactory.getLogger(TimedInvocationsInteractor.class.getName());

    private final InteractorCommons interactorCommons;
    private final InvokerManager invokerManager;

    @Override
    public void execute(final Parameters parameters) {
        InvocationPlan invocationPlan = interactorCommons.createInvocationPlan(parameters);
        LOG.info("Invoking Invocation Plan");
        Stopwatch stopwatchedInvocations = invokerManager.invoke(invocationPlan, parameters.getTolerableErrorPercentage());
        LOG.info("Invocations finished in {} ns", stopwatchedInvocations.elapsed(TimeUnit.NANOSECONDS));
    }
}
