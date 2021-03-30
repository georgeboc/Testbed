package com.testbed.boundary.executors.spark;

import com.clearspring.analytics.util.Lists;
import com.testbed.boundary.executors.Result;

import java.util.List;

public class NoResult implements Result {
    @Override
    public Object getValues() {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public List<String> getColumnNames() {
        return Lists.newArrayList();
    }
}
