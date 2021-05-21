package com.testbed.interactors.monitors;

import com.testbed.entities.invocations.InvocationPlan;

import java.util.concurrent.Callable;

public interface Monitor {
    MonitoringInformation monitor(final Callable<MonitoringInformation> callable, final InvocationPlan invocationPlan);
}
