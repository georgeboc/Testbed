package com.testbed.interactors.converters.dispatchers;

import com.testbed.entities.operations.deserialized.DeserializedGroupBy;

import java.util.stream.Stream;

public class GroupByInputTagStreamDispatcher implements Dispatcher<DeserializedGroupBy, Stream<String>> {
    @Override
    public Stream<String> dispatch(Object object) {
        DeserializedGroupBy deserializedGroupBy = (DeserializedGroupBy) object;
        return Stream.of(deserializedGroupBy.getInputTag());
    }
}
