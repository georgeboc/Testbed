package com.testbed.boundary.metrics.prometheus;

import com.testbed.boundary.metrics.prometheus.schemas.InstantSchema;
import com.testbed.boundary.metrics.prometheus.schemas.RangeSchema;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.time.Instant;

public interface PrometheusAPI {
    @GET("query")
    Call<InstantSchema> instantQuery(@Query("query") String query);

    @GET("query_range")
    Call<RangeSchema> rangeQuery(@Query("query") String query,
                                 @Query("start") Instant start,
                                 @Query("end") Instant end,
                                 @Query("step") double stepInSeconds);
}