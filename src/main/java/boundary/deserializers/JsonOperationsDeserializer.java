package boundary.deserializers;

import com.fasterxml.jackson.databind.ObjectMapper;
import entities.operations.deserialized.DeserializedOperation;
import entities.operations.deserialized.DeserializedOperations;

import java.io.IOException;

public class JsonOperationsDeserializer implements OperationsDeserializer {
    @Override
    public DeserializedOperations deserialize(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.addMixInAnnotations(DeserializedOperation.class, DeserializedOperationMixin.class);
        return mapper.readValue(jsonString, DeserializedOperations.class);
    }
}
