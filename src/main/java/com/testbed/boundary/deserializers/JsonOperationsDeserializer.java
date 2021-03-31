package com.testbed.boundary.deserializers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.testbed.entities.operations.deserialized.DeserializedOperations;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@RequiredArgsConstructor
public class JsonOperationsDeserializer implements Deserializer<DeserializedOperations> {
    private final ObjectMapper objectMapper;

    @Override
    public DeserializedOperations deserialize(final String path) {
        String fileContents = tryReadFileContents(path);
        return tryReadValueFromMapper(objectMapper, fileContents);
    }

    private DeserializedOperations tryReadValueFromMapper(final ObjectMapper mapper, final String fileContents) {
        try {
            return mapper.readValue(fileContents, DeserializedOperations.class);
        } catch (JsonProcessingException exception) {
            throw new RuntimeException(exception);
        }
    }

    private String tryReadFileContents(final String path) {
        try {
            return Files.readString(Paths.get(path));
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
