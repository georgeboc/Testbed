package com.testbed.interactors.monitors;

import com.testbed.entities.invocations.InvocationPlan;

public interface Monitor {
    MonitoringInformation monitor(Runnable runnable, InvocationPlan invocationPlan);
}
