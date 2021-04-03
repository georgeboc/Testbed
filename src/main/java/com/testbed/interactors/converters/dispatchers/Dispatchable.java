package com.testbed.interactors.converters.dispatchers;

public interface Dispatchable {
    Object accept(DispatcherHandler dispatcherHandler);
}
