package com.testbed.interactors.monitors;

import com.testbed.boundary.metrics.InstantMetric;
import com.testbed.boundary.metrics.MetricsQuery;
import com.testbed.boundary.metrics.RangeMetric;
import com.testbed.entities.invocations.InvocationPlan;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang.WordUtils;

import java.time.Instant;
import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

import static com.testbed.interactors.monitors.MonitorCommons.coalesce;

@RequiredArgsConstructor
public class MinMemoryUtilizationMonitor implements Monitor {
    private static final String MONITOR_PREFIX = "node";
    private static final String MONITOR_SUFFIX = "MinMemoryUtilizationInBytes";
    private static final String QUERY = "node_memory_MemTotal_bytes - (node_memory_MemFree_bytes + " +
            "node_memory_Cached_bytes + node_memory_Buffers_bytes)";
    private static final double STEP_IN_SECONDS = 1.0;

    private final MetricsQuery metricsQuery;

    @SneakyThrows
    @Override
    public MonitoringInformation monitor(Callable<MonitoringInformation> callable, InvocationPlan invocationPlan) {
        Instant start = Instant.now();
        MonitoringInformation callableMonitoringInformation = callable.call();
        Instant end = Instant.now();
        Map<String, RangeMetric> rangeResultByHostname = metricsQuery.getRangeQueryByHostname(QUERY, start, end, STEP_IN_SECONDS);
        Map<String, String> aggregatedValues = aggregate(rangeResultByHostname, Math::min);
        Map<String, String> aggregatedValuesWithMonitorName = addMonitorName(aggregatedValues);
        return coalesce(callableMonitoringInformation, new MonitoringInformation(aggregatedValuesWithMonitorName));
    }

    private Map<String, String> aggregate(Map<String, RangeMetric> rangeResultByHostname, BinaryOperator<Long> function) {
        return rangeResultByHostname.entrySet().stream()
                .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), reduce(entry, function)))
                .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), String.valueOf(entry.getValue())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private long reduce(Map.Entry<String, RangeMetric> entry, BinaryOperator<Long> function) {
        return entry.getValue().getInstantMetrics().stream().map(InstantMetric::getValue).reduce(function).get();
    }

    private Map<String, String> addMonitorName(Map<String, String> valuesByHostname) {
        return valuesByHostname.entrySet().stream()
                .map(entry -> new AbstractMap.SimpleEntry<>(getMonitorName(entry.getKey()), entry.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    }

    private String getMonitorName(String hostname) {
        return MONITOR_PREFIX + WordUtils.capitalizeFully(hostname) + MONITOR_SUFFIX;
    }
}
