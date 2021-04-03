package com.testbed.interactors.converters.dispatchers;

public interface DispatcherHandler {
    <T> void assignDispatcherToClass(Dispatcher<T, ?> dispatcher, Class<?> clazz);
    Object visit(Object objectToVisit);
}
