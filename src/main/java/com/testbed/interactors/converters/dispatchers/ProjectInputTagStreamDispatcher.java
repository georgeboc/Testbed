package com.testbed.interactors.converters.dispatchers;

import com.testbed.entities.operations.deserialized.DeserializedProject;

import java.util.stream.Stream;

public class ProjectInputTagStreamDispatcher implements Dispatcher<DeserializedProject, Stream<String>> {
    @Override
    public Stream<String> dispatch(Object object) {
        DeserializedProject deserializedProject = (DeserializedProject) object;
        return Stream.of(deserializedProject.getInputTag());
    }
}
