package com.testbed.boundary.invocations.intermediateDatasets.instrumentation;

import com.testbed.boundary.invocations.intermediateDatasets.IntermediateDataset;

import java.util.List;

public interface IntermediateDatasetInstrumentation {
    long count(final IntermediateDataset intermediateDataset);
    List<String> getColumnNames(final IntermediateDataset intermediateDataset);
}
