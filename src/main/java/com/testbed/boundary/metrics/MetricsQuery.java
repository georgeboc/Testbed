package com.testbed.boundary.metrics;

import java.time.Instant;
import java.util.Map;

public interface MetricsQuery {
    Map<String, InstantMetric> getInstantQueryByHostname(final String query);
    Map<String, RangeMetric> getRangeQueryByHostname(final String query,
                                                     final Instant start,
                                                     final Instant end,
                                                     final double stepInSeconds);
}
