package com.testbed.boundary.invocations;

import com.testbed.boundary.invocations.intermediateDatasets.IntermediateDataset;

public interface Invokable {
    IntermediateDataset invoke(final InvocationParameters invocationParameters);
}
