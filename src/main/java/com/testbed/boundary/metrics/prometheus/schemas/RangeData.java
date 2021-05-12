package com.testbed.boundary.metrics.prometheus.schemas;

import lombok.Data;

import java.util.List;

@Data
public class RangeData {
    private String resultType;
    private List<RangeResult> result;
}
