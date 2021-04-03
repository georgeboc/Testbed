package com.testbed.interactors.converters.dispatchers;

import com.testbed.entities.operations.deserialized.DeserializedSelect;

import java.util.stream.Stream;

public class SelectInputTagStreamDispatcher implements Dispatcher<DeserializedSelect, Stream<String>> {
    @Override
    public Stream<String> dispatch(Object object) {
        DeserializedSelect deserializedSelect = (DeserializedSelect) object;
        return Stream.of(deserializedSelect.getInputTag());
    }
}
