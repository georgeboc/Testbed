package com.testbed.interactors.monitors;

import com.testbed.entities.invocations.InvocationPlan;

public class NoMonitor implements Monitor {
    @Override
    public MonitoringInformation monitor(Runnable runnable, InvocationPlan invocationPlan) {
        runnable.run();
        return MonitoringInformation.createNew();
    }
}
