package com.testbed.interactors.converters.dispatchers;

import com.google.common.collect.Maps;
import com.testbed.entities.operations.deserialized.DeserializedJoin;
import com.testbed.entities.operations.deserialized.DeserializedLoad;
import com.testbed.entities.operations.deserialized.DeserializedProject;
import com.testbed.entities.operations.deserialized.DeserializedSelect;

public class DispatchersFactory {
    public DispatcherHandler getDispatcherHandlerForDeserializedLoadFilter() {
        DispatcherHandler dispatcherHandler = new GenericDispatcherHandler(Maps.newHashMap());
        dispatcherHandler.assignDispatcherToClass(new FilterInDeserializedLoadDispatcher(), DeserializedLoad.class);
        return dispatcherHandler;
    }

    public DispatcherHandler getDispatcherHandlerForInputTagStreamWithoutLoadOperation() {
        DispatcherHandler dispatcherHandler = new GenericDispatcherHandler(Maps.newHashMap());
        dispatcherHandler.assignDispatcherToClass(new SelectInputTagStreamDispatcher(), DeserializedSelect.class);
        dispatcherHandler.assignDispatcherToClass(new ProjectInputTagStreamDispatcher(), DeserializedProject.class);
        dispatcherHandler.assignDispatcherToClass(new JoinInputTagStreamDispatcher(), DeserializedJoin.class);
        return dispatcherHandler;
    }
}
