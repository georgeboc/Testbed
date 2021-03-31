package com.testbed.boundary.invocations.results;

import com.clearspring.analytics.util.Lists;

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
