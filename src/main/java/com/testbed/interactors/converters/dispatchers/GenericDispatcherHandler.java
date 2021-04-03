package com.testbed.interactors.converters.dispatchers;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class GenericDispatcherHandler implements DispatcherHandler {
    private final Map<Class<?>, Dispatcher<?, ?>> classToVisitorMap;

    @Override
    public <T> void assignDispatcherToClass(Dispatcher<T, ?> deserializedLoadDispatcher, Class<?> clazz) {
        classToVisitorMap.put(clazz, deserializedLoadDispatcher);
    }

    @Override
    public Object visit(Object objectToVisit) {
        Dispatcher<?, ?> dispatcher = classToVisitorMap.get(objectToVisit.getClass());
        if (dispatcher != null) {
            return dispatcher.dispatch(objectToVisit);
        }
        return null;
    }
}
