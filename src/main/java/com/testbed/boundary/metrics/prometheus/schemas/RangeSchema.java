package com.testbed.boundary.metrics.prometheus.schemas;

import lombok.Data;

@Data
public class RangeSchema {
    private String status;
    private RangeData data;
}
