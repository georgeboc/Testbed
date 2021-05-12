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
    private static final String MONITOR_PREFIX = "Node";
    private static final String WRITES_MONITOR_SUFFIX = "LocalFileSystemWrittenBytes";
    private static final String READS_MONITOR_SUFFIX = "LocalFileSystemReadBytes";

    private final MonitoringInformationCoalesce monitoringInformationCoalesce;
    private final MetricsQuery metricsQuery;
    private final String deviceName;

    @SneakyThrows
    @Override
    public MonitoringInformation monitor(Callable<MonitoringInformation> callable, InvocationPlan invocationPlan) {
        String writesQuery = String.format("node_disk_written_bytes_total{device='%s'}", deviceName);
        String readsQuery = String.format("node_disk_read_bytes_total{device='%s'}", deviceName);
        Map<String, InstantMetric> initialInstantMetricByHostnameWrites = metricsQuery.getInstantMetricByHostname(writesQuery);
        Map<String, InstantMetric> initialInstantMetricByHostnameReads = metricsQuery.getInstantMetricByHostname(readsQuery);
        MonitoringInformation callableMonitoringInformation = callable.call();
        Map<String, InstantMetric> finalInstantMetricByHostnameWrites = metricsQuery.getInstantMetricByHostname(writesQuery);
        Map<String, InstantMetric> finalInstantMetricByHostnameReads = metricsQuery.getInstantMetricByHostname(readsQuery);
        Map<String, String> differences = getDifferencesByHostname(WRITES_MONITOR_SUFFIX,
                initialInstantMetricByHostnameWrites,
                finalInstantMetricByHostnameWrites);
        differences.putAll(getDifferencesByHostname(READS_MONITOR_SUFFIX,
                initialInstantMetricByHostnameReads,
                finalInstantMetricByHostnameReads));
        return monitoringInformationCoalesce.coalesce(callableMonitoringInformation,
                new MonitoringInformation(differences));
    }

    private Map<String, String> getDifferencesByHostname(String monitorSuffix,
                                                         Map<String, InstantMetric> initialInstantMetricByHost,
                                                         Map<String, InstantMetric> finalInstantMetricByHost) {
        return initialInstantMetricByHost.entrySet().stream()
                .map(entry -> getDifferenceEntry(monitorSuffix, finalInstantMetricByHost, entry))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Map.Entry<String, String> getDifferenceEntry(String monitorSuffix,
            Map<String, InstantMetric> finalInstantMetricByHostname,
            Map.Entry<String, InstantMetric> initialInstantMetricEntry) {
        String hostname = initialInstantMetricEntry.getKey();
        InstantMetric initialInstantMetric = initialInstantMetricEntry.getValue();
        InstantMetric finalInstantMetric = finalInstantMetricByHostname.get(hostname);
        long difference = finalInstantMetric.getValue() - initialInstantMetric.getValue();
        return new AbstractMap.SimpleEntry<>(getMonitorName(monitorSuffix, hostname), String.valueOf(difference));
    }

    private String getMonitorName(String monitorSuffix, String hostname) {
        return MONITOR_PREFIX + WordUtils.capitalizeFully(hostname) + monitorSuffix;
    }
}
