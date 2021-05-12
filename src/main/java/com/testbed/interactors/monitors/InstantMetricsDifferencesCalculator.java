package com.testbed.interactors.monitors;

import com.testbed.boundary.metrics.InstantMetric;
import com.testbed.boundary.metrics.MetricsQuery;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang.WordUtils;

import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import static com.testbed.interactors.monitors.MonitorCommons.coalesce;

@RequiredArgsConstructor
public class InstantMetricsDifferencesCalculator {
    private final MetricsQuery metricsQuery;

    @SneakyThrows
    public MonitoringInformation calculate(Callable<MonitoringInformation> callable,
                                           String query,
                                           String monitorNamePrefix,
                                           String monitorNameSuffix) {
        Map<String, InstantMetric> initialInstantMetricByHostname = metricsQuery.getInstantQueryByHostname(query);
        MonitoringInformation callableMonitoringInformation = callable.call();
        Map<String, InstantMetric> finalInstantMetricByHostname = metricsQuery.getInstantQueryByHostname(query);
        return coalesce(callableMonitoringInformation,
                getMonitorInformation(monitorNamePrefix,
                        monitorNameSuffix,
                        initialInstantMetricByHostname,
                        finalInstantMetricByHostname));
    }

    public MonitoringInformation getMonitorInformation(String monitorNamePrefix,
                                                       String monitorNameSuffix,
                                                       Map<String, InstantMetric> initialInstantMetricByHost,
                                                       Map<String, InstantMetric> finalInstantMetricByHost) {
        Map<String, String> differences = initialInstantMetricByHost.entrySet().stream()
                .map(entry -> getDifferenceEntry(monitorNamePrefix, monitorNameSuffix, finalInstantMetricByHost, entry))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return new MonitoringInformation(differences);
    }

    public Map.Entry<String, String> getDifferenceEntry(String monitorNamePrefix,
                                                        String monitorNameSuffix,
                                                        Map<String, InstantMetric> finalInstantMetricByHostname,
                                                        Map.Entry<String, InstantMetric> initialInstantMetricEntry) {
        String hostname = initialInstantMetricEntry.getKey();
        InstantMetric initialInstantMetric = initialInstantMetricEntry.getValue();
        InstantMetric finalInstantMetric = finalInstantMetricByHostname.get(hostname);
        long difference = finalInstantMetric.getValue() - initialInstantMetric.getValue();
        return new AbstractMap.SimpleEntry<>(getMonitorName(monitorNamePrefix, monitorNameSuffix, hostname),
                String.valueOf(difference));
    }

    public String getMonitorName(String monitorNamePrefix, String monitorNameSuffix, String hostname) {
        return monitorNamePrefix + WordUtils.capitalizeFully(hostname) + monitorNameSuffix;
    }
}
