package com.testbed.interactors.converters.dispatchers;

import com.google.common.collect.Maps;
import com.testbed.entities.operations.deserialized.BinaryDeserializedOperation;
import com.testbed.entities.operations.deserialized.DeserializedLoad;
import com.testbed.entities.operations.deserialized.UnaryDeserializedOperation;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@RequiredArgsConstructor
public class DispatchersFactory {
    private final Dispatcher<DeserializedLoad, DeserializedLoad> filterInDeserializedLoadDispatcher;
    private final Dispatcher<UnaryDeserializedOperation, Stream<String>> unaryInputTagStreamDispatcher;
    private final Dispatcher<BinaryDeserializedOperation, Stream<String>> binaryStreamDispatcher;

    public DispatcherHandler getDispatcherHandlerForDeserializedLoadFilter() {
        DispatcherHandler dispatcherHandler = new ClassDispatcherHandler(Maps.newHashMap());
        dispatcherHandler.assignDispatcherToClass(filterInDeserializedLoadDispatcher, DeserializedLoad.class);
        return dispatcherHandler;
    }

    public DispatcherHandler getDispatcherHandlerForInputTagStreamWithoutLoadOperation() {
        DispatcherHandler dispatcherHandler = new InterfaceDispatcherHandler(Maps.newHashMap());
        dispatcherHandler.assignDispatcherToClass(unaryInputTagStreamDispatcher, UnaryDeserializedOperation.class);
        dispatcherHandler.assignDispatcherToClass(binaryStreamDispatcher, BinaryDeserializedOperation.class);
        return dispatcherHandler;
    }
}
