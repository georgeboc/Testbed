package com.testbed.interactors.converters.dispatchers;

import com.google.common.collect.Maps;
import com.testbed.entities.operations.deserialized.DeserializedGroupBy;
import com.testbed.entities.operations.deserialized.DeserializedJoin;
import com.testbed.entities.operations.deserialized.DeserializedLoad;
import com.testbed.entities.operations.deserialized.DeserializedProject;
import com.testbed.entities.operations.deserialized.DeserializedSelect;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@RequiredArgsConstructor
public class DispatchersFactory {
    private final Dispatcher<DeserializedLoad, DeserializedLoad> filterInDeserializedLoadDispatcher;
    private final Dispatcher<DeserializedSelect, Stream<String>> selectInputTagStreamDispatcher;
    private final Dispatcher<DeserializedProject, Stream<String>> projectStreamDispatcher;
    private final Dispatcher<DeserializedJoin, Stream<String>> joinStreamDispatcher;
    private final Dispatcher<DeserializedGroupBy, Stream<String>> groupByStreamDispatcher;

    public DispatcherHandler getDispatcherHandlerForDeserializedLoadFilter() {
        DispatcherHandler dispatcherHandler = new GenericDispatcherHandler(Maps.newHashMap());
        dispatcherHandler.assignDispatcherToClass(filterInDeserializedLoadDispatcher, DeserializedLoad.class);
        return dispatcherHandler;
    }

    public DispatcherHandler getDispatcherHandlerForInputTagStreamWithoutLoadOperation() {
        DispatcherHandler dispatcherHandler = new GenericDispatcherHandler(Maps.newHashMap());
        dispatcherHandler.assignDispatcherToClass(selectInputTagStreamDispatcher, DeserializedSelect.class);
        dispatcherHandler.assignDispatcherToClass(projectStreamDispatcher, DeserializedProject.class);
        dispatcherHandler.assignDispatcherToClass(joinStreamDispatcher, DeserializedJoin.class);
        dispatcherHandler.assignDispatcherToClass(groupByStreamDispatcher, DeserializedGroupBy.class);
        return dispatcherHandler;
    }
}
