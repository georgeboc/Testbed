package com.testbed.interactors.monitors;

import com.testbed.entities.invocations.InvocationPlan;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.Callable;

@RequiredArgsConstructor
public class NetworkTransmittedBytesMonitor implements Monitor {
    private static final String MONITOR_NAME_PREFIX = "node";
    private static final String MONITOR_NAME_SUFFIX = "NetworkTransmittedBytes";
    private static final String QUERY = "sum by(instance) (node_network_transmit_bytes_total{device=~'eth.*'})";

    private final InstantMetricsDifferencesCalculator instantMetricsDifferencesCalculator;

    @Override
    public MonitoringInformation monitor(final Callable<MonitoringInformation> callable,
                                         final InvocationPlan invocationPlan) {
        return instantMetricsDifferencesCalculator.calculate(InstantMetricsDifferencesCalculatorParameters.builder()
                .monitorNameParameters(MonitorNameParameters.builder()
                        .monitorNamePrefix(MONITOR_NAME_PREFIX)
                        .monitorNameSuffix(MONITOR_NAME_SUFFIX)
                        .build())
                .callable(callable)
                .query(QUERY)
                .build());
    }
}
