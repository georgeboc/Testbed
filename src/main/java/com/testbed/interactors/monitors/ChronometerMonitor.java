package com.testbed.interactors.monitors;

import com.google.common.base.Stopwatch;
import com.testbed.entities.invocations.InvocationPlan;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class ChronometerMonitor implements Monitor {
    private static final String INVOCATION_TIME_IN_NANOSECONDS = "invocationTimeInNanoseconds";

    private final MonitoringInformationCoalesce monitoringInformationCoalesce;

    @Override
    public MonitoringInformation monitor(Callable<MonitoringInformation> callable,
                                         InvocationPlan invocationPlan) {
        Stopwatch stopWatch = Stopwatch.createStarted();
        MonitoringInformation callableMonitoringInformation = MonitorCommons.tryCall(callable);
        stopWatch.stop();
        return monitoringInformationCoalesce.coalesce(callableMonitoringInformation, getMonitoringInformation(stopWatch));
    }

    private MonitoringInformation getMonitoringInformation(Stopwatch stopWatch) {
        MonitoringInformation monitoringInformation = MonitoringInformation.createNew();
        monitoringInformation.getResult().put(INVOCATION_TIME_IN_NANOSECONDS,
                String.valueOf(stopWatch.elapsed(TimeUnit.NANOSECONDS)));
        return monitoringInformation;
    }
}
