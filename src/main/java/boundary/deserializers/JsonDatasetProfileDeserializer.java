package boundary.deserializers;

import com.fasterxml.jackson.databind.ObjectMapper;
import entities.profiles.ColumnarProfile;
import entities.profiles.DatasetProfile;

import java.io.IOException;

public class JsonDatasetProfileDeserializer implements DatasetProfileDeserializer {
    @Override
    public DatasetProfile deserialize(String jsonString) throws RuntimeException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.addMixInAnnotations(ColumnarProfile.class, ColumnarProfilerMixin.class);
        return tryReadValue(mapper, jsonString);
    }

    private DatasetProfile tryReadValue(ObjectMapper mapper, String jsonString) throws RuntimeException {
        try {
            return mapper.readValue(jsonString, DatasetProfile.class);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
