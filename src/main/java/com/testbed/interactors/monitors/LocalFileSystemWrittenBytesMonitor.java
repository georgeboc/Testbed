package com.testbed.interactors.monitors;

import com.testbed.entities.invocations.InvocationPlan;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.concurrent.Callable;

@RequiredArgsConstructor
public class LocalFileSystemWrittenBytesMonitor implements Monitor {
    private static final String MONITOR_PREFIX = "node";
    private static final String MONITOR_SUFFIX = "LocalFileSystemWrittenBytes";

    private final InstantMetricsDifferencesCalculator instantMetricsDifferencesCalculator;
    private final String deviceName;

    @Override
    public MonitoringInformation monitor(Callable<MonitoringInformation> callable, InvocationPlan invocationPlan) {
        String query = String.format("node_disk_written_bytes_total{device='%s'}", deviceName);
        return instantMetricsDifferencesCalculator.calculate(callable, query, MONITOR_PREFIX, MONITOR_SUFFIX);
    }
}
