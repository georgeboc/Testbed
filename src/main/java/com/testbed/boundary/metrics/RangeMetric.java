package com.testbed.boundary.metrics;

import lombok.Data;

import java.util.List;

@Data
public class RangeMetric {
    private final List<InstantMetric> instantMetrics;
}
