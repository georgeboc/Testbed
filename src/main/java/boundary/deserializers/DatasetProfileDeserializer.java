package boundary.deserializers;

import entities.profiles.DatasetProfile;

public interface DatasetProfileDeserializer {
    DatasetProfile deserialize(String jsonString) throws RuntimeException;
}
