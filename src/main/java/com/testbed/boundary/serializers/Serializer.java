package com.testbed.boundary.serializers;

public interface Serializer<T> {
    void serialize(final String path, final T object);
}
