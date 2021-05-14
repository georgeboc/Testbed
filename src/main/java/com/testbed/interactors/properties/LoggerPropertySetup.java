package com.testbed.interactors.properties;

import lombok.SneakyThrows;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoggerPropertySetup {
    private static final String CURRENT_TIMESTAMP = "current_timestamp";
    private static final String HOSTNAME = "hostname";

    @SneakyThrows
    public static void setup() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-hh_mm_ss-SS");
        System.setProperty(HOSTNAME, InetAddress.getLocalHost().getHostName());
        System.setProperty(CURRENT_TIMESTAMP, dateFormat.format(new Date()));
    }
}
