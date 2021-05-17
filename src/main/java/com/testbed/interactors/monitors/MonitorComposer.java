package com.testbed.interactors.monitors;

import com.testbed.entities.invocations.InvocationPlan;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Function;

@RequiredArgsConstructor
public class MonitorComposer implements Monitor {
    private final List<Monitor> monitors;

    @Override
    public MonitoringInformation monitor(Callable<MonitoringInformation> callable, InvocationPlan invocationPlan) {
        return monitors.stream()
                .map(monitor -> getFunction(monitor, invocationPlan))
                .reduce(Function::andThen)
                .map(function -> function.apply(callable))
                .map(MonitorCommons::tryCall)
                .orElseGet(MonitoringInformation::createNew);
    }

    private Function<Callable<MonitoringInformation>, Callable<MonitoringInformation>> getFunction(Monitor monitor,
                                                                                                   InvocationPlan invocationPlan) {
        return callable -> () -> monitor.monitor(callable, invocationPlan);
    }
}
