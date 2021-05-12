package com.testbed.interactors.monitors;

import com.testbed.entities.invocations.InvocationPlan;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.Callable;

@RequiredArgsConstructor
public class AverageSwapUtilizationMonitor implements Monitor {
    private static final String MONITOR_NAME_PREFIX = "node";
    private static final String MONITOR_NAME_SUFFIX = "AverageSwapUtilizationInBytes";
    private static final String QUERY = "node_memory_SwapCached_bytes";

    private final RangeMetricsAggregateCalculator rangeMetricsAggregateCalculator;

    @Override
    public MonitoringInformation monitor(Callable<MonitoringInformation> callable, InvocationPlan invocationPlan) {
        return rangeMetricsAggregateCalculator.calculate(RangeMetricsAggregateCalculatorParameters.builder()
                .monitorNameParameters(MonitorNameParameters.builder()
                        .monitorNamePrefix(MONITOR_NAME_PREFIX)
                        .monitorNameSuffix(MONITOR_NAME_SUFFIX)
                        .build())
                .aggregationFunction(MonitorCommons::getAverage)
                .callable(callable)
                .query(QUERY)
                .build());
    }
}
