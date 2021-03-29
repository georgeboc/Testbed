package com.testbed.boundary.serializers;

public interface Serializer<T> {
    void serialize(String path, T object);
}
