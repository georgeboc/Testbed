package com.testbed.interactors.converters.dispatchers;

import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class ClassDispatcherHandler implements DispatcherHandler {
    private final Map<Class<?>, Dispatcher<?, ?>> classToVisitorMap;

    @Override
    public <T> void assignDispatcherToClass(Dispatcher<T, ?> deserializedLoadDispatcher, Class<?> clazz) {
        classToVisitorMap.put(clazz, deserializedLoadDispatcher);
    }

    @Override
    public Object visit(Object objectToVisit) {
        Optional<? extends Dispatcher<?, ?>> optionalDispatcher = Optional.ofNullable(classToVisitorMap
                .get(objectToVisit.getClass()));
        return optionalDispatcher.map(dispatcher -> dispatcher.dispatch(objectToVisit)).orElse(null);
    }
}
