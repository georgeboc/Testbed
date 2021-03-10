package boundary.deserializers;

import entities.operations.deserialized.DeserializedOperations;

import java.io.IOException;

public interface OperationsDeserializer {
    DeserializedOperations deserialize(String jsonString) throws IOException;
}
