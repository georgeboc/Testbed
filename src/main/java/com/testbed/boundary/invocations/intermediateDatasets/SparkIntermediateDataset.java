package com.testbed.boundary.invocations.intermediateDatasets;

import lombok.Data;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.Optional;

@Data
public class SparkIntermediateDataset implements IntermediateDataset {
    private final Dataset<Row> value;

    @Override
    public Optional<Object> getValue() {
        return Optional.of(value);
    }
}
