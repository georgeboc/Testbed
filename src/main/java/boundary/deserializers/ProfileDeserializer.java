package boundary.deserializers;

import entities.profiles.Profile;

public interface ProfileDeserializer {
    Profile deserialize(String path) throws RuntimeException;
}
