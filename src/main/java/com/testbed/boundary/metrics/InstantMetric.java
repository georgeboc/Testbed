package com.testbed.boundary.metrics;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class InstantMetric {
    private final Instant instant;
    private final long value;
}
