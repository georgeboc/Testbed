package com.testbed.boundary.deserializers;

public interface Deserializer<T> {
    T deserialize(String path) throws RuntimeException;
}
