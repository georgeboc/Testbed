package com.testbed.boundary.invocations.results;

import lombok.Data;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.Arrays;
import java.util.List;

@Data
public class SparkResult implements Result {
    private final Dataset<Row> values;

    @Override
    public long count() {
        return values.count();
    }

    @Override
    public List<String> getColumnNames() {
        return Arrays.asList(values.columns());
    }
}
