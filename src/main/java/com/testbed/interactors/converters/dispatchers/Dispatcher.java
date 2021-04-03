package com.testbed.interactors.converters.dispatchers;

public interface Dispatcher<T, S> {
    S dispatch(Object object);
}
