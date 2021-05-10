package com.testbed.interactors.monitors;

import com.google.common.base.Stopwatch;
import com.testbed.entities.invocations.InvocationPlan;

import java.util.concurrent.TimeUnit;

public class ChronometerMonitor implements Monitor {
    private static final String INVOCATION_TIME_IN_NANOSECONDS = "invocationTimeInNanoseconds";

    @Override
    public MonitoringInformation monitor(Runnable runnable, InvocationPlan invocationPlan) {
        Stopwatch stopWatch = Stopwatch.createStarted();
        runnable.run();
        stopWatch.stop();
        MonitoringInformation monitoringInformation = MonitoringInformation.createNew();
        monitoringInformation.getResult().put(INVOCATION_TIME_IN_NANOSECONDS,
                String.valueOf(stopWatch.elapsed(TimeUnit.NANOSECONDS)));
        return monitoringInformation;
    }
}
