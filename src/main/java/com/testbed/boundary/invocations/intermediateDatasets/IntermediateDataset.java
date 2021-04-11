package com.testbed.boundary.invocations.intermediateDatasets;

import java.util.List;

public interface IntermediateDataset {
    Object getValue();
    long count();
    List<String> getColumnNames();
}
