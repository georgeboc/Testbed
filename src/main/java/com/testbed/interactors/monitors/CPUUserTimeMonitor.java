package com.testbed.interactors.monitors;

import com.testbed.entities.invocations.InvocationPlan;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.concurrent.Callable;

@RequiredArgsConstructor
public class CPUUserTimeMonitor implements Monitor {
    private static final String MONITOR_PREFIX = "node";
    private static final String MONITOR_SUFFIX = "CpuTimeUserModeInSeconds";
    private static final String QUERY = "sum by (instance) (node_cpu_seconds_total{mode='user'})";

    private final InstantMetricsDifferencesCalculator instantMetricsDifferencesCalculator;

    @SneakyThrows
    @Override
    public MonitoringInformation monitor(Callable<MonitoringInformation> callable, InvocationPlan invocationPlan) {
        return instantMetricsDifferencesCalculator.calculate(callable, QUERY, MONITOR_PREFIX, MONITOR_SUFFIX);
    }
}
