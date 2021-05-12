package com.testbed.boundary.metrics;

import java.time.Instant;
import java.util.Map;

public interface MetricsQuery {
    Map<String, InstantMetric> getInstantQueryByHostname(String query);
    Map<String, RangeMetric> getRangeQueryByHostname(String query, Instant start, Instant end, double stepInSeconds);
}
