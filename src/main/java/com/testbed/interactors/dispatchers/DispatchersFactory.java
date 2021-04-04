package com.testbed.interactors.dispatchers;

import com.google.common.collect.Maps;
import com.testbed.entities.operations.deserialized.BinaryDeserializedOperation;
import com.testbed.entities.operations.deserialized.DeserializedLoad;
import com.testbed.entities.operations.deserialized.UnaryDeserializedOperation;
import com.testbed.entities.operations.deserialized.ZeroaryDeserializedOperation;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@RequiredArgsConstructor
public class DispatchersFactory {
    private final Dispatcher<DeserializedLoad, DeserializedLoad> filterInDeserializedLoadDispatcher;

    private final Dispatcher<UnaryDeserializedOperation, Stream<String>> unaryInputTagStreamDispatcher;
    private final Dispatcher<BinaryDeserializedOperation, Stream<String>> binaryInputTagStreamDispatcher;

    public DispatcherManager getDispatcherManagerForDeserializedLoadFilter() {
        DispatcherManager dispatcherManager = new InterfaceDispatcherManager(Maps.newHashMap());
        dispatcherManager.assignDispatcherToClass(filterInDeserializedLoadDispatcher, ZeroaryDeserializedOperation.class);
        return dispatcherManager;
    }

    public DispatcherManager getDispatcherManagerForInputTagStreamWithoutLoadOperation() {
        DispatcherManager dispatcherManager = new InterfaceDispatcherManager(Maps.newHashMap());
        dispatcherManager.assignDispatcherToClass(unaryInputTagStreamDispatcher, UnaryDeserializedOperation.class);
        dispatcherManager.assignDispatcherToClass(binaryInputTagStreamDispatcher, BinaryDeserializedOperation.class);
        return dispatcherManager;
    }
}
