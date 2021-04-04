package com.testbed.interactors.converters.dispatchers;

import com.testbed.entities.operations.deserialized.DeserializedAggregate;

import java.util.stream.Stream;

public class AggregateInputTagStreamDispatcher implements Dispatcher<DeserializedAggregate, Stream<String>> {
    @Override
    public Stream<String> dispatch(Object object) {
        DeserializedAggregate deserializedAggregate = (DeserializedAggregate) object;
        return Stream.of(deserializedAggregate.getInputTag());
    }
}
