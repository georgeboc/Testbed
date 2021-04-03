package com.testbed.interactors.converters.dispatchers;

import com.testbed.entities.operations.deserialized.DeserializedJoin;

import java.util.stream.Stream;

public class JoinInputTagStreamDispatcher implements Dispatcher<DeserializedJoin, Stream<String>> {
    @Override
    public Stream<String> dispatch(Object object) {
        DeserializedJoin deserializedJoin = (DeserializedJoin) object;
        return Stream.of(deserializedJoin.getLeftInputTag(), deserializedJoin.getRightInputTag());
    }
}
