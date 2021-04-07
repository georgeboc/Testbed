package com.testbed.interactors.converters.deserializedToLogical.inputTagStream;

import com.testbed.entities.operations.deserialized.BinaryDeserializedOperation;
import com.testbed.entities.operations.deserialized.DeserializedOperation;

import java.util.stream.Stream;

public class BinaryInputTagsStream implements InputTagsStream{
    public Stream<String> getInputTagStream(DeserializedOperation deserializedOperation) {
        BinaryDeserializedOperation binaryDeserializedOperation = (BinaryDeserializedOperation) deserializedOperation;
        return Stream.of(binaryDeserializedOperation.getLeftInputTag(), binaryDeserializedOperation.getRightInputTag());
    }
}
