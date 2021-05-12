package com.testbed.interactors.monitors;

import com.testbed.entities.invocations.InvocationPlan;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.time.Instant;
import java.util.concurrent.Callable;

@RequiredArgsConstructor
public class ExecutionInstantsMonitor implements Monitor {
    private static final String INITIAL_INSTANT = "initialInstant";
    private static final String FINAL_INSTANT = "finalInstant";

    private final MonitoringInformationCoalesce monitoringInformationCoalesce;

    @SneakyThrows
    @Override
    public MonitoringInformation monitor(Callable<MonitoringInformation> callable,
                                         InvocationPlan invocationPlan) {
        Instant initialInstant = Instant.now();
        MonitoringInformation callableMonitoringInformation = callable.call();
        Instant finalInstant = Instant.now();
        return monitoringInformationCoalesce.coalesce(callableMonitoringInformation,
                getMonitoringInformation(initialInstant, finalInstant));
    }

    private MonitoringInformation getMonitoringInformation(Instant initialInstant, Instant finalInstant) {
        MonitoringInformation monitoringInformation = MonitoringInformation.createNew();
        monitoringInformation.getResult().put(INITIAL_INSTANT, initialInstant.toString());
        monitoringInformation.getResult().put(FINAL_INSTANT, finalInstant.toString());
        return monitoringInformation;
    }
}
