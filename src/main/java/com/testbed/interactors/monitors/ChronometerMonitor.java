package com.testbed.interactors.monitors;

import com.google.common.base.Stopwatch;
import com.testbed.entities.invocations.InvocationPlan;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static com.testbed.interactors.monitors.MonitorCommons.coalesce;

@RequiredArgsConstructor
public class ChronometerMonitor implements Monitor {
    private static final String INVOCATION_TIME_IN_NANOSECONDS = "invocationTimeInNanoseconds";

    @Override
    public MonitoringInformation monitor(final Callable<MonitoringInformation> callable,
                                         final InvocationPlan invocationPlan) {
        Stopwatch stopWatch = Stopwatch.createStarted();
        MonitoringInformation callableMonitoringInformation = MonitorCommons.tryCall(callable);
        stopWatch.stop();
        return coalesce(callableMonitoringInformation, getMonitoringInformation(stopWatch));
    }

    private MonitoringInformation getMonitoringInformation(final Stopwatch stopWatch) {
        MonitoringInformation monitoringInformation = MonitoringInformation.createNew();
        monitoringInformation.getResult().put(INVOCATION_TIME_IN_NANOSECONDS,
                String.valueOf(stopWatch.elapsed(TimeUnit.NANOSECONDS)));
        return monitoringInformation;
    }
}
