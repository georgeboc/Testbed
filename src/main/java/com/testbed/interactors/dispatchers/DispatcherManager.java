package com.testbed.interactors.dispatchers;

public interface DispatcherManager {
    <T> void assignDispatcherToClass(Dispatcher<T, ?> dispatcher, Class<?> clazz);
    Object visit(Object objectToVisit);
}
