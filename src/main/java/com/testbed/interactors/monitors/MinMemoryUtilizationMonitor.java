package com.testbed.interactors.monitors;

import com.testbed.entities.invocations.InvocationPlan;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.Callable;

@RequiredArgsConstructor
public class MinMemoryUtilizationMonitor implements Monitor {
    private static final String MONITOR_NAME_PREFIX = "node";
    private static final String MONITOR_NAME_SUFFIX = "MinMemoryUtilizationInBytes";
    private static final String QUERY = "node_memory_MemTotal_bytes - (node_memory_MemFree_bytes + " +
            "node_memory_Cached_bytes + node_memory_Buffers_bytes)";

    private final RangeMetricsAggregateCalculator rangeMetricsAggregateCalculator;


    @Override
    public MonitoringInformation monitor(Callable<MonitoringInformation> callable, InvocationPlan invocationPlan) {
        return rangeMetricsAggregateCalculator.calculate(RangeMetricsAggregateCalculatorParameters.builder()
                .monitorNameParameters(MonitorNameParameters.builder()
                        .monitorNamePrefix(MONITOR_NAME_PREFIX)
                        .monitorNameSuffix(MONITOR_NAME_SUFFIX)
                        .build())
                .aggregationFunction(MonitorCommons::getMin)
                .callable(callable)
                .query(QUERY)
                .build());
    }
}
