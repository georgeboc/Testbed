package com.testbed.boundary.deserializers;

public interface Deserializer<T> {
    T deserialize(final String path) throws RuntimeException;
}
