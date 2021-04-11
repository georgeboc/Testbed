package com.testbed.boundary.invocations.intermediateDatasets;

import com.clearspring.analytics.util.Lists;

import java.util.List;

public class NoIntermediateDataset implements IntermediateDataset {
    @Override
    public Object getValue() {
        return new Object();
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
