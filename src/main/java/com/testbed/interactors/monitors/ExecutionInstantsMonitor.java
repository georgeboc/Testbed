package com.testbed.interactors.monitors;

import com.testbed.entities.invocations.InvocationPlan;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.concurrent.Callable;

import static com.testbed.interactors.monitors.MonitorCommons.coalesce;

@RequiredArgsConstructor
public class ExecutionInstantsMonitor implements Monitor {
    private static final String INITIAL_INSTANT = "initialInstant";
    private static final String FINAL_INSTANT = "finalInstant";

    @Override
    public MonitoringInformation monitor(final Callable<MonitoringInformation> callable,
                                         final InvocationPlan invocationPlan) {
        Instant initialInstant = Instant.now();

        MonitoringInformation callableMonitoringInformation = MonitorCommons.tryCall(callable);

        Instant finalInstant = Instant.now();
        return coalesce(callableMonitoringInformation, getMonitoringInformation(initialInstant, finalInstant));
    }

    private MonitoringInformation getMonitoringInformation(final Instant initialInstant, final Instant finalInstant) {
        MonitoringInformation monitoringInformation = MonitoringInformation.createNew();
        monitoringInformation.getResult().put(INITIAL_INSTANT, initialInstant.toString());
        monitoringInformation.getResult().put(FINAL_INSTANT, finalInstant.toString());
        return monitoringInformation;
    }
}
