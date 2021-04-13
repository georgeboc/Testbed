package com.testbed.boundary.invocations.intermediateDatasets;

import java.util.Optional;

public class NoIntermediateDataset implements IntermediateDataset {
    @Override
    public Optional<Object> getValue() {
        return Optional.empty();
    }
}
