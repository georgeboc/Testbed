package com.testbed.boundary.metrics.prometheus.schemas;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Metric {
    @SerializedName("__name__")
    private String name;
    private String instance;
    private String job;
}
