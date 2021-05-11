package com.testbed.boundary.metrics;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface MetricsQuery {
    Map<String, InstantMetric> getInstantMetricByHostname(String query) throws IOException;
}
