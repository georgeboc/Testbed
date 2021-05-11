package com.testbed.interactors.monitors;

import com.testbed.entities.invocations.InvocationPlan;
import lombok.SneakyThrows;

import java.util.concurrent.Callable;

public class NoMonitor implements Monitor {
    @SneakyThrows
    @Override
    public MonitoringInformation monitor(Callable<MonitoringInformation> callable, InvocationPlan invocationPlan) {
        return callable.call();
    }
}
