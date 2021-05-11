package com.testbed.interactors.monitors;

import com.testbed.boundary.metrics.InstantMetric;
import com.testbed.boundary.metrics.MetricsQuery;
import com.testbed.entities.invocations.InvocationPlan;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang.WordUtils;

import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class LocalFileSystemMonitor implements Monitor {
    private static final String MONITOR_PREFIX = "localFileSystemOnNode";

    private final MonitoringInformationCoalesce monitoringInformationCoalesce;
    private final MetricsQuery metricsQuery;
    private final String deviceName;

    @SneakyThrows
    @Override
    public MonitoringInformation monitor(Callable<MonitoringInformation> callable, InvocationPlan invocationPlan) {
        String query = String.format("node_disk_written_bytes_total{device='%s'}", deviceName);
        Map<String, InstantMetric> initialInstantMetricByHostname = metricsQuery.getInstantMetricByHostname(query);
        MonitoringInformation callableMonitoringInformation = callable.call();
        Map<String, InstantMetric> finalInstantMetricByHostname = metricsQuery.getInstantMetricByHostname(query);
        return monitoringInformationCoalesce.coalesce(callableMonitoringInformation,
                getMonitoringInformation(initialInstantMetricByHostname, finalInstantMetricByHostname));
    }

    private MonitoringInformation getMonitoringInformation(Map<String, InstantMetric> initialInstantMetricByHost,
                                                           Map<String, InstantMetric> finalInstantMetricByHost) {
        Map<String, String> differencesByHostname = initialInstantMetricByHost.entrySet().stream()
                .map(entry -> getDifferenceEntry(finalInstantMetricByHost, entry))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return new MonitoringInformation(differencesByHostname);
    }

    private Map.Entry<String, String> getDifferenceEntry(Map<String, InstantMetric> finalInstantMetricByHostname,
                                    Map.Entry<String, InstantMetric> initialInstantMetricEntry) {
        String hostname = initialInstantMetricEntry.getKey();
        InstantMetric initialInstantMetric = initialInstantMetricEntry.getValue();
        InstantMetric finalInstantMetric = finalInstantMetricByHostname.get(hostname);
        long difference = finalInstantMetric.getValue() - initialInstantMetric.getValue();
        return new AbstractMap.SimpleEntry<>(getMonitorName(hostname), String.valueOf(difference));
    }

    private String getMonitorName(String hostname) {
        return MONITOR_PREFIX + WordUtils.capitalizeFully(hostname);
    }
}
