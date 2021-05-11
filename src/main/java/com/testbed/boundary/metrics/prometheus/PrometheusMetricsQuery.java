package com.testbed.boundary.metrics.prometheus;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.testbed.boundary.metrics.InstantMetric;
import com.testbed.boundary.metrics.MetricsQuery;
import com.testbed.boundary.metrics.prometheus.schemas.InstantData;
import com.testbed.boundary.metrics.prometheus.schemas.Result;
import lombok.RequiredArgsConstructor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.time.Instant;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("UnstableApiUsage")
@RequiredArgsConstructor
public class PrometheusMetricsQuery implements MetricsQuery {
    private static final int TIMESTAMP_POSITION = 0;
    private static final int VALUE_POSITION = 1;

    private final String baseUrl;

    @Override
    public Map<String, InstantMetric> getInstantMetricByHostname(String query) throws IOException {
        Call<InstantData> call = getCall(query);
        Response<InstantData> instantDataResponse = call.execute();
        return getInstantMetricByHostnameFromResponse(instantDataResponse);
    }

    private Call<InstantData> getCall(String query) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        PrometheusAPI prometheusAPI = retrofit.create(PrometheusAPI.class);
        return prometheusAPI.instantQuery(query);
    }

    private Map<String, InstantMetric> getInstantMetricByHostnameFromResponse(Response<InstantData> instantDataResponse) {
        Preconditions.checkArgument(instantDataResponse.body() != null,
                "Instant Data Response body is null");
        InstantData instantData = instantDataResponse.body();
        return instantData.getData().getResults().stream()
                .map(this::getInstantMetricByHostname)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Map.Entry<String, InstantMetric> getInstantMetricByHostname(Result result) {
        List<String> values = result.getValues();
        long timestamp = (long) Double.parseDouble(values.get(TIMESTAMP_POSITION));
        long value = Long.parseLong(values.get(VALUE_POSITION));
        InstantMetric instantMetric = InstantMetric.builder()
                .instant(Instant.ofEpochMilli(timestamp))
                .value(value)
                .build();
        return new AbstractMap.SimpleEntry<>(getHostnameFromInstance(result.getMetric().getInstance()), instantMetric);
    }

    private String getHostnameFromInstance(String instance) {
        return Arrays.stream(instance.split(":")).findFirst().get();
    }
}
