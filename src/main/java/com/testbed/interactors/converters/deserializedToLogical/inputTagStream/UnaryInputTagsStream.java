package com.testbed.interactors.converters.deserializedToLogical.inputTagStream;

import com.testbed.entities.operations.deserialized.DeserializedOperation;
import com.testbed.entities.operations.deserialized.UnaryDeserializedOperation;

import java.util.stream.Stream;

public class UnaryInputTagsStream implements InputTagsStream {
    public Stream<String> getInputTagStream(DeserializedOperation deserializedOperation) {
        UnaryDeserializedOperation unaryDeserializedOperation = (UnaryDeserializedOperation) deserializedOperation;
        return Stream.of(unaryDeserializedOperation.getInputTag());
    }
}
