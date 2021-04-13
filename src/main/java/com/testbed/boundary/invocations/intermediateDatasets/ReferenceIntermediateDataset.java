package com.testbed.boundary.invocations.intermediateDatasets;

import lombok.Data;

import java.util.Optional;

@Data
public class ReferenceIntermediateDataset implements IntermediateDataset {
    private final String value;

    @Override
    public Optional<Object> getValue() {
        return Optional.of(value);
    }
}
