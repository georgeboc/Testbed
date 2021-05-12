package com.testbed.interactors.monitors;

import lombok.Builder;
import lombok.Data;

import java.util.concurrent.Callable;

@Data
@Builder
public class InstantMetricsDifferencesCalculatorParameters {
    private final Callable<MonitoringInformation> callable;
    private final String query;
    private final MonitorNameParameters monitorNameParameters;
}
