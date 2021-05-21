package com.testbed.boundary.metrics.prometheus;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.testbed.boundary.metrics.InstantMetric;
import com.testbed.boundary.metrics.MetricsQuery;
import com.testbed.boundary.metrics.RangeMetric;
import com.testbed.boundary.metrics.prometheus.schemas.InstantResult;
import com.testbed.boundary.metrics.prometheus.schemas.InstantSchema;
import com.testbed.boundary.metrics.prometheus.schemas.RangeResult;
import com.testbed.boundary.metrics.prometheus.schemas.RangeSchema;
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

@RequiredArgsConstructor
public class PrometheusMetricsQuery implements MetricsQuery {
    private static final int TIMESTAMP_POSITION = 0;
    private static final int VALUE_POSITION = 1;

    private final String baseUrl;

    @Override
    public Map<String, InstantMetric> getInstantQueryByHostname(final String query) {
        Call<InstantSchema> call = getPrometheusAPI().instantQuery(query);
        Response<InstantSchema> instantResponse = tryExecuteCall(call);
        return getInstantMetricByHostnameFromResponse(instantResponse);
    }

    private <T> Response<T> tryExecuteCall(final Call<T> call) {
        try {
            return call.execute();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public Map<String, RangeMetric> getRangeQueryByHostname(final String query,
                                                            final Instant start,
                                                            final Instant end,
                                                            final double stepInSeconds) {
        Call<RangeSchema> call = getPrometheusAPI().rangeQuery(query, start, end, stepInSeconds);
        Response<RangeSchema> rangeResponse = tryExecuteCall(call);
        return getInstantMetricsByHostnameFromResponse(rangeResponse);
    }

    private PrometheusAPI getPrometheusAPI() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit.create(PrometheusAPI.class);
    }

    private Map<String, InstantMetric> getInstantMetricByHostnameFromResponse(final Response<InstantSchema> instantResponse) {
        Preconditions.checkArgument(instantResponse.body() != null,
                "Instant Response body is null");
        InstantSchema instantSchema = instantResponse.body();
        return instantSchema.getData().getResult().stream()
                .map(this::getInstantMetricByHostname)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Map.Entry<String, InstantMetric> getInstantMetricByHostname(final InstantResult instantResult) {
        List<String> valueList = instantResult.getValue();
        InstantMetric instantMetric = getInstantMetric(valueList);
        return new AbstractMap.SimpleEntry<>(getHostnameFromInstance(instantResult.getMetric().getInstance()), instantMetric);
    }

    private InstantMetric getInstantMetric(final List<String> valueList) {
        long timestamp = (long) Double.parseDouble(valueList.get(TIMESTAMP_POSITION));
        long value = (long) Double.parseDouble(valueList.get(VALUE_POSITION));
        return InstantMetric.builder()
                .instant(Instant.ofEpochMilli(timestamp))
                .value(value)
                .build();
    }

    private String getHostnameFromInstance(final String instance) {
        return Arrays.stream(instance.split(":")).findFirst().get();
    }

    private Map<String, RangeMetric> getInstantMetricsByHostnameFromResponse(final Response<RangeSchema> rangeResponse) {
        Preconditions.checkArgument(rangeResponse.body() != null,
                "Range Response body is null");
        RangeSchema rangeSchema = rangeResponse.body();
        return rangeSchema.getData().getResult().stream()
                .map(this::getRangeMetricByHostname)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Map.Entry<String, RangeMetric> getRangeMetricByHostname(final RangeResult rangeResult) {
        List<List<String>> valuesList = rangeResult.getValues();
        List<InstantMetric> instantMetrics = valuesList.stream()
                .map(this::getInstantMetric)
                .collect(Collectors.toList());
        return new AbstractMap.SimpleEntry<>(getHostnameFromInstance(rangeResult.getMetric().getInstance()),
                new RangeMetric(instantMetrics));
    }
}
