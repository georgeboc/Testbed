package com.testbed.boundary.metrics.prometheus.schemas;

import lombok.Data;

import java.util.List;

@Data
public class InstantResult {
    private Metric metric;
    private List<String> value;
}
