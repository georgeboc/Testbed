package com.testbed.boundary.deserializers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.testbed.entities.operations.deserialized.DeserializedOperations;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

@RequiredArgsConstructor
public class JsonOperationsDeserializer implements Deserializer<DeserializedOperations> {
    private final FileSystem fileSystem;
    private final ObjectMapper objectMapper;

    @Override
    public DeserializedOperations deserialize(final String path) {
        String fileContents = tryReadFileContents(path);
        return tryReadValueFromMapper(objectMapper, fileContents);
    }

    private DeserializedOperations tryReadValueFromMapper(final ObjectMapper mapper, final String fileContents) {
        try {
            return mapper.readValue(fileContents, DeserializedOperations.class);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private String tryReadFileContents(final String path) {
        try {
            InputStream inputstream = fileSystem.open(new Path(path));
            return IOUtils.toString(inputstream, Charset.defaultCharset());
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
