package com.testbed.interactors.dispatchers.inputTagStream;

import com.testbed.entities.operations.deserialized.UnaryDeserializedOperation;
import com.testbed.interactors.dispatchers.Dispatcher;

import java.util.stream.Stream;

public class UnaryInputTagStreamDispatcher implements Dispatcher<UnaryDeserializedOperation, Stream<String>> {
    @Override
    public Stream<String> dispatch(Object object) {
        UnaryDeserializedOperation unaryDeserializedOperation = (UnaryDeserializedOperation) object;
        return Stream.of(unaryDeserializedOperation.getInputTag());
    }
}
