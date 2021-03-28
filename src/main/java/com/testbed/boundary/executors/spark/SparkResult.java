package com.testbed.boundary.executors.spark;

import com.testbed.boundary.executors.Result;
import lombok.Data;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

@Data
public class SparkResult implements Result {
    private final Dataset<Row> value;
}
