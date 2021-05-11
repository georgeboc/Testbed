package com.testbed.boundary.metrics.prometheus;

import com.testbed.boundary.metrics.prometheus.schemas.InstantData;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PrometheusAPI {
    @GET("query")
    Call<InstantData> instantQuery(@Query("query") String query);
}