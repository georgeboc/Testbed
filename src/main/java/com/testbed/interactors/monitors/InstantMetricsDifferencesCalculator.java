package com.testbed.interactors.monitors;

import com.testbed.boundary.metrics.InstantMetric;
import com.testbed.boundary.metrics.MetricsQuery;
import lombok.RequiredArgsConstructor;

import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.testbed.interactors.monitors.MonitorCommons.coalesce;
import static com.testbed.interactors.monitors.MonitorCommons.getMonitorName;

@RequiredArgsConstructor
public class InstantMetricsDifferencesCalculator {
    private final MetricsQuery metricsQuery;

    public MonitoringInformation calculate(InstantMetricsDifferencesCalculatorParameters parameters) {
        Map<String, InstantMetric> initialInstantMetricByHostname = metricsQuery.getInstantQueryByHostname(parameters.getQuery());
        MonitoringInformation callableMonitoringInformation = MonitorCommons.tryCall(parameters.getCallable());
        Map<String, InstantMetric> finalInstantMetricByHostname = metricsQuery.getInstantQueryByHostname(parameters.getQuery());
        return coalesce(callableMonitoringInformation,
                getMonitorInformation(parameters.getMonitorNameParameters(),
                        initialInstantMetricByHostname,
                        finalInstantMetricByHostname));
    }

    public MonitoringInformation getMonitorInformation(MonitorNameParameters monitorNameParameters,
                                                       Map<String, InstantMetric> initialInstantMetricByHost,
                                                       Map<String, InstantMetric> finalInstantMetricByHost) {
        Map<String, String> differences = initialInstantMetricByHost.entrySet().stream()
                .map(entry -> getDifferenceEntry(monitorNameParameters, finalInstantMetricByHost, entry))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return new MonitoringInformation(differences);
    }

    public Map.Entry<String, String> getDifferenceEntry(MonitorNameParameters monitorNameParameters,
                                                        Map<String, InstantMetric> finalInstantMetricByHostname,
                                                        Map.Entry<String, InstantMetric> initialInstantMetricEntry) {
        String hostname = initialInstantMetricEntry.getKey();
        InstantMetric initialInstantMetric = initialInstantMetricEntry.getValue();
        InstantMetric finalInstantMetric = finalInstantMetricByHostname.get(hostname);
        long difference = finalInstantMetric.getValue() - initialInstantMetric.getValue();
        return new AbstractMap.SimpleEntry<>(getMonitorName(monitorNameParameters, hostname),
                String.valueOf(difference));
    }
}
