package com.testbed.interactors.converters.dispatchers;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
public class InterfaceDispatcherHandler implements DispatcherHandler {
    private final Map<Class<?>, Dispatcher<?, ?>> classToVisitorMap;

    @Override
    public <T> void assignDispatcherToClass(Dispatcher<T, ?> deserializedLoadDispatcher, Class<?> clazz) {
        classToVisitorMap.put(clazz, deserializedLoadDispatcher);
    }

    @Override
    public Object visit(Object objectToVisit) {
        Optional<? extends Dispatcher<?, ?>> optionalDispatcher = Arrays.stream(objectToVisit.getClass().getInterfaces())
                .map(classToVisitorMap::get)
                .filter(Objects::nonNull)
                .findFirst();
        return optionalDispatcher.map(dispatcher -> dispatcher.dispatch(objectToVisit)).orElse(null);
    }
}
