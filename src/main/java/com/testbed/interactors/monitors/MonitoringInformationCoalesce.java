package com.testbed.interactors.monitors;

import com.google.common.collect.Maps;

import java.util.Map;

public class MonitoringInformationCoalesce {
    public MonitoringInformation coalesce(MonitoringInformation first, MonitoringInformation second) {
        Map<String, String> result = Maps.newHashMap();
        first.getResult().forEach(result::put);
        second.getResult().forEach(result::put);
        return new MonitoringInformation(result);
    }
}
