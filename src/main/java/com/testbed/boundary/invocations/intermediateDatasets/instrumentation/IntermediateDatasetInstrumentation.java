package com.testbed.boundary.invocations.intermediateDatasets.instrumentation;

import com.testbed.boundary.invocations.intermediateDatasets.IntermediateDataset;

import java.util.List;

public interface IntermediateDatasetInstrumentation {
    long count(IntermediateDataset intermediateDataset);
    List<String> getColumnNames(IntermediateDataset intermediateDataset);
}
