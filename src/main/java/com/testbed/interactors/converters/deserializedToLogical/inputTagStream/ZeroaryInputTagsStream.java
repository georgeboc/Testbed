package com.testbed.interactors.converters.deserializedToLogical.inputTagStream;

import com.testbed.entities.operations.deserialized.DeserializedOperation;

import java.util.stream.Stream;

public class ZeroaryInputTagsStream implements InputTagsStream {
    public Stream<String> getInputTagStream(final DeserializedOperation deserializedOperation) {
        return Stream.of();
    }
}
