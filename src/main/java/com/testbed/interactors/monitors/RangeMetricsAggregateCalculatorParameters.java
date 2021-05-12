package com.testbed.interactors.monitors;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Function;

@Data
@Builder
public class RangeMetricsAggregateCalculatorParameters {
    private final Callable<MonitoringInformation> callable;
    private final String query;
    private final Function<List<Long>, Long> aggregationFunction;
    private final MonitorNameParameters monitorNameParameters;
}
