package com.testbed.interactors.dispatchers;

public interface Dispatchable {
    Object accept(DispatcherManager dispatcherManager);
}
