package com.testbed.boundary.metrics.prometheus.schemas;

import lombok.Data;

import java.util.List;

@Data
public class RangeResult {
    private Metric metric;
    private List<List<String>> values;
}
