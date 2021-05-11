package com.testbed.boundary.metrics.prometheus.schemas;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@lombok.Data
public class Data {
    private String resultType;
    @SerializedName("result")
    private List<Result> results;
}
