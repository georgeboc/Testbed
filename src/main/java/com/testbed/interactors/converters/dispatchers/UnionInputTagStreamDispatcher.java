package com.testbed.interactors.converters.dispatchers;

import com.testbed.entities.operations.deserialized.DeserializedUnion;

import java.util.stream.Stream;

public class UnionInputTagStreamDispatcher implements Dispatcher<DeserializedUnion, Stream<String>> {
    @Override
    public Stream<String> dispatch(Object object) {
        DeserializedUnion deserializedUnion = (DeserializedUnion) object;
        return Stream.of(deserializedUnion.getLeftInputTag(), deserializedUnion.getRightInputTag());
    }
}
