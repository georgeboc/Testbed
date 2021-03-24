package boundary.deserializers;

import com.fasterxml.jackson.databind.ObjectMapper;
import entities.operations.deserialized.DeserializedOperation;
import entities.operations.deserialized.DeserializedOperations;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonOperationsDeserializer implements OperationsDeserializer {
    @Override
    public DeserializedOperations deserialize(String path) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.addMixIn(DeserializedOperation.class, DeserializedOperationMixin.class);
        return mapper.readValue(Files.readString(Paths.get(path)), DeserializedOperations.class);
    }
}
