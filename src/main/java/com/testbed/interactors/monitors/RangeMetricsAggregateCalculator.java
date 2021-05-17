package com.testbed.interactors.monitors;

import com.testbed.boundary.metrics.InstantMetric;
import com.testbed.boundary.metrics.MetricsQuery;
import com.testbed.boundary.metrics.RangeMetric;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.testbed.interactors.monitors.MonitorCommons.coalesce;
import static com.testbed.interactors.monitors.MonitorCommons.getMonitorName;

@RequiredArgsConstructor
public class RangeMetricsAggregateCalculator {
    private static final double STEP_IN_SECONDS = 1.0;
    private final MetricsQuery metricsQuery;

    public MonitoringInformation calculate(RangeMetricsAggregateCalculatorParameters parameters) {
        Instant start = Instant.now();
        MonitoringInformation callableMonitoringInformation = MonitorCommons.tryCall(parameters.getCallable());
        Instant end = Instant.now();
        Map<String, RangeMetric> rangeResultByHostname = metricsQuery.getRangeQueryByHostname(parameters.getQuery(),
                start,
                end,
                STEP_IN_SECONDS);
        return coalesce(callableMonitoringInformation, getMonitorInformation(rangeResultByHostname, parameters));
    }

    private MonitoringInformation getMonitorInformation(Map<String, RangeMetric> rangeResultByHostname,
                                                        RangeMetricsAggregateCalculatorParameters parameters) {
        Map<String, String> aggregatedValues = aggregateValues(rangeResultByHostname, parameters);
        return new MonitoringInformation(aggregatedValues);
    }

    private Map<String, String> aggregateValues(Map<String, RangeMetric> rangeResultByHostname,
                                                RangeMetricsAggregateCalculatorParameters parameters) {
        return rangeResultByHostname.entrySet().stream()
                .map(entry -> aggregateEntryValues(entry, parameters))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Map.Entry<String, String> aggregateEntryValues(Map.Entry<String, RangeMetric> rangeMetricEntry,
                                                           RangeMetricsAggregateCalculatorParameters parameters) {
        String monitorName = getMonitorName(parameters.getMonitorNameParameters(), rangeMetricEntry.getKey());
        String reductionStringValue = String.valueOf(aggregate(rangeMetricEntry, parameters.getAggregationFunction()));
        return new AbstractMap.SimpleEntry<>(monitorName, reductionStringValue);
    }

    private long aggregate(Map.Entry<String, RangeMetric> entry, Function<List<Long>, Long> aggregationFunction) {
        List<Long> values = entry.getValue().getInstantMetrics().stream()
                .map(InstantMetric::getValue)
                .collect(Collectors.toList());
        return aggregationFunction.apply(values);
    }
}
