package com.testbed.boundary.deserializers;

import com.testbed.entities.profiles.Profile;

public interface ProfileDeserializer {
    Profile deserialize(String path) throws RuntimeException;
}
