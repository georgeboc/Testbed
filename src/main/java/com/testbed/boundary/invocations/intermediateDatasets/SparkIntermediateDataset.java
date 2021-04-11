package com.testbed.boundary.invocations.intermediateDatasets;

import lombok.Data;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.Arrays;
import java.util.List;

@Data
public class SparkIntermediateDataset implements IntermediateDataset {
    private final Dataset<Row> value;

    @Override
    public long count() {
        return value.count();
    }

    @Override
    public List<String> getColumnNames() {
        return Arrays.asList(value.columns());
    }
}
