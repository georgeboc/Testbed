package com.testbed.boundary.deserializers;

import com.testbed.entities.operations.deserialized.DeserializedOperations;

import java.io.IOException;

public interface OperationsDeserializer {
    DeserializedOperations deserialize(String path) throws IOException;
}
