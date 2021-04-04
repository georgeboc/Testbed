package com.testbed.interactors.converters.dispatchers;

import com.testbed.entities.operations.deserialized.UnaryDeserializedOperation;

import java.util.stream.Stream;

public class UnaryInputTagStreamDispatcher implements Dispatcher<UnaryDeserializedOperation, Stream<String>> {
    @Override
    public Stream<String> dispatch(Object object) {
        UnaryDeserializedOperation unaryDeserializedOperation = (UnaryDeserializedOperation) object;
        return Stream.of(unaryDeserializedOperation.getInputTag());
    }
}
