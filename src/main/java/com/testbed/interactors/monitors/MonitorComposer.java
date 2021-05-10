package com.testbed.interactors.monitors;

import com.testbed.entities.invocations.InvocationPlan;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.function.Function;

@RequiredArgsConstructor
public class MonitorComposer implements Monitor {
    private final Monitor firstMonitor;
    private final List<Monitor> monitors;
    private final MonitoringInformationCoalesce monitoringInformationCoalesce;

    @Override
    public MonitoringInformation monitor(Runnable runnable, InvocationPlan invocationPlan) {
        return monitors.stream()
                .map(monitor -> monitorToFunction(monitor, runnable, invocationPlan))
                .reduce(monitorToFunction(firstMonitor, runnable, invocationPlan), Function::andThen)
                .apply(MonitoringInformation.createNew());
    }

    public Function<MonitoringInformation, MonitoringInformation> monitorToFunction(Monitor monitor,
                                                                                    Runnable runnable,
                                                                                    InvocationPlan invocationPlan) {
        return (monitorResult) -> monitoringInformationCoalesce.coalesce(monitorResult,
                monitor.monitor(runnable, invocationPlan));
    }
}
