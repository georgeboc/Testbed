package com.testbed.interactors.dispatchers;

public interface Dispatcher<T, S> {
    S dispatch(Object object);
}
