package com.testbed.interactors.monitors;

import com.google.common.collect.Maps;
import org.apache.commons.lang.WordUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class MonitorCommons {
    public static MonitoringInformation coalesce(final MonitoringInformation first, final MonitoringInformation second) {
        Map<String, String> coalescedMap = Maps.newHashMap();
        first.getResult().forEach(coalescedMap::put);
        second.getResult().forEach(coalescedMap::put);
        return new MonitoringInformation(coalescedMap);
    }

    public static MonitoringInformation tryCall(final Callable<MonitoringInformation> callable) {
        try {
            return callable.call();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public static String getMonitorName(final MonitorNameParameters monitorNameParameters, final String hostname) {
        return monitorNameParameters.getMonitorNamePrefix() + WordUtils.capitalizeFully(hostname) +
                monitorNameParameters.getMonitorNameSuffix();
    }

    public static Long getMin(final List<Long> values) {
        return values.stream().reduce(Math::min).get();
    }

    public static Long getMax(final List<Long> values) {
        return values.stream().reduce(Math::max).get();
    }

    public static Long getAverage(final List<Long> values) {
        return (long) ((double) values.stream().reduce(Math::addExact).get()/values.size());
    }
}
