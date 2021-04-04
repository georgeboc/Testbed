package com.testbed.interactors.dispatchers.inputTagStream;

import com.testbed.entities.operations.deserialized.BinaryDeserializedOperation;
import com.testbed.interactors.dispatchers.Dispatcher;

import java.util.stream.Stream;

public class BinaryInputTagStreamDispatcher implements Dispatcher<BinaryDeserializedOperation, Stream<String>> {
    @Override
    public Stream<String> dispatch(Object object) {
        BinaryDeserializedOperation binaryDeserializedOperation = (BinaryDeserializedOperation) object;
        return Stream.of(binaryDeserializedOperation.getLeftInputTag(), binaryDeserializedOperation.getRightInputTag());
    }
}
