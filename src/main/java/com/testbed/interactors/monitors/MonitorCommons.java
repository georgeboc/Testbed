package com.testbed.interactors.monitors;

import com.google.common.collect.Maps;
import org.apache.commons.lang.WordUtils;

import java.util.Map;

public class MonitorCommons {
    public static MonitoringInformation coalesce(MonitoringInformation first, MonitoringInformation second) {
        Map<String, String> coalescedMap = Maps.newHashMap();
        first.getResult().forEach(coalescedMap::put);
        second.getResult().forEach(coalescedMap::put);
        return new MonitoringInformation(coalescedMap);
    }

    public static String getMonitorName(MonitorNameParameters monitorNameParameters, String hostname) {
        return monitorNameParameters.getMonitorNamePrefix() + WordUtils.capitalizeFully(hostname) +
                monitorNameParameters.getMonitorNameSuffix();
    }
}
