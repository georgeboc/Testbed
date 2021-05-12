package com.testbed.interactors.monitors;

import com.testbed.entities.invocations.InvocationPlan;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.Callable;

@RequiredArgsConstructor
public class LocalFileSystemReadBytesMonitor implements Monitor {
    private static final String MONITOR_NAME_PREFIX = "node";
    private static final String MONITOR_NAME_SUFFIX = "LocalFileSystemReadBytes";

    private final InstantMetricsDifferencesCalculator instantMetricsDifferencesCalculator;
    private final String deviceName;

    @Override
    public MonitoringInformation monitor(Callable<MonitoringInformation> callable, InvocationPlan invocationPlan) {
        String query = String.format("node_disk_read_bytes_total{device='%s'}", deviceName);
        return instantMetricsDifferencesCalculator.calculate(InstantMetricsDifferencesCalculatorParameters.builder()
                .monitorNameParameters(MonitorNameParameters.builder()
                        .monitorNamePrefix(MONITOR_NAME_PREFIX)
                        .monitorNameSuffix(MONITOR_NAME_SUFFIX)
                        .build())
                .callable(callable)
                .query(query)
                .build());
    }
}
