package com.testbed.interactors.monitors;

import com.testbed.entities.invocations.InvocationPlan;

import java.util.concurrent.Callable;

public class NoMonitor implements Monitor {
    @Override
    public MonitoringInformation monitor(final Callable<MonitoringInformation> callable,
                                         final InvocationPlan invocationPlan) {
        return MonitorCommons.tryCall(callable);
    }
}
