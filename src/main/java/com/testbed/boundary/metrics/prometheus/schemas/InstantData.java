package com.testbed.boundary.metrics.prometheus.schemas;

import lombok.Data;

import java.util.List;


@Data
public class InstantData {
    private String resultType;
    private List<InstantResult> result;
}
