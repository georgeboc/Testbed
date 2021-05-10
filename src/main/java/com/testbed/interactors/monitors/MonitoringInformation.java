package com.testbed.interactors.monitors;

import com.google.common.collect.Maps;
import lombok.Data;

import java.util.Map;

@Data
public class MonitoringInformation {
    private final Map<String, String> result;

    public static MonitoringInformation createNew() {
        return new MonitoringInformation(Maps.newHashMap());
    }
}
