package com.testbed.interactors.monitors;

import com.testbed.entities.invocations.InvocationPlan;

import java.util.concurrent.Callable;

public class NoMonitor implements Monitor {
    @Override
    public MonitoringInformation monitor(Callable<MonitoringInformation> callable, InvocationPlan invocationPlan) {
        return MonitorCommons.tryCall(callable);
    }
}
