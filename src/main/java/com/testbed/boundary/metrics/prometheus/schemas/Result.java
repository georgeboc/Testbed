package com.testbed.boundary.metrics.prometheus.schemas;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class Result {
    private Metric metric;
    @SerializedName("value")
    private List<String> values;
}
