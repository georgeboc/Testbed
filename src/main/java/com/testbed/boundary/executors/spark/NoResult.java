package com.testbed.boundary.executors.spark;

import com.testbed.boundary.executors.Result;

public class NoResult implements Result {
    @Override
    public Object getValues() {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }
}
