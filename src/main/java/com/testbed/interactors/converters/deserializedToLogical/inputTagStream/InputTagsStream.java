package com.testbed.interactors.converters.deserializedToLogical.inputTagStream;

import com.testbed.entities.operations.deserialized.DeserializedOperation;

import java.util.stream.Stream;

public interface InputTagsStream {
    Stream<String> getInputTagStream(final DeserializedOperation deserializedOperation);
}
