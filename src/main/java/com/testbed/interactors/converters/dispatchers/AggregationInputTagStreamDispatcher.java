package com.testbed.interactors.converters.dispatchers;

import com.testbed.entities.operations.deserialized.DeserializedAggregation;

import java.util.stream.Stream;

public class AggregationInputTagStreamDispatcher implements Dispatcher<DeserializedAggregation, Stream<String>> {
    @Override
    public Stream<String> dispatch(Object object) {
        DeserializedAggregation deserializedAggregation = (DeserializedAggregation) object;
        return Stream.of(deserializedAggregation.getInputTag());
    }
}
