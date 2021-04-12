package com.testbed.boundary.invocations.intermediateDatasets;

import lombok.Data;

import java.util.List;

@Data
public class ReferenceIntermediateDataset implements IntermediateDataset {
    private final String value;

    // TODO: Implement
    @Override
    public long count() {
        return 0;
    }

    // TODO: Implement
    @Override
    public List<String> getColumnNames() {
        return null;
    }
}
