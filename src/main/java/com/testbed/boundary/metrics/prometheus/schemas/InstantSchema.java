package com.testbed.boundary.metrics.prometheus.schemas;

import lombok.Data;

@Data
public class InstantSchema {
    private String status;
    private InstantData data;
}
