package com.testbed.boundary.deserializers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.testbed.entities.operations.deserialized.DeserializedOperation;
import com.testbed.entities.operations.deserialized.DeserializedOperations;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonOperationsDeserializer implements Deserializer<DeserializedOperations> {
    @Override
    public DeserializedOperations deserialize(String path) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.addMixIn(DeserializedOperation.class, DeserializedOperationMixin.class);
        String fileContents = tryReadFileContents(path);
        return tryReadValueFromMapper(mapper, fileContents);
    }

    private DeserializedOperations tryReadValueFromMapper(ObjectMapper mapper, String fileContents) {
        try {
            return mapper.readValue(fileContents, DeserializedOperations.class);
        } catch (JsonProcessingException exception) {
            throw new RuntimeException(exception);
        }
    }

    private String tryReadFileContents(String path) {
        try {
            return Files.readString(Paths.get(path));
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
