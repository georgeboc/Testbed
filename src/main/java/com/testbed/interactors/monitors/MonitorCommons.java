package com.testbed.interactors.monitors;

import java.util.concurrent.Callable;

public class MonitorCommons {
    public static MonitoringInformation tryCall(Callable<MonitoringInformation> callable) {
        try {
            return callable.call();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
