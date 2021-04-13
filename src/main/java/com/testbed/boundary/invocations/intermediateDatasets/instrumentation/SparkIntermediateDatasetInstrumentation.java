package com.testbed.boundary.invocations.intermediateDatasets.instrumentation;

import com.testbed.boundary.invocations.intermediateDatasets.IntermediateDataset;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SparkIntermediateDatasetInstrumentation implements IntermediateDatasetInstrumentation{
    @Override
    public long count(IntermediateDataset intermediateDataset) {
        if (intermediateDataset.getValue().isEmpty()) {
            return 0L;
        }
        return ((Dataset<Row>) intermediateDataset.getValue().get()).count();
    }

    @Override
    public List<String> getColumnNames(IntermediateDataset intermediateDataset) {
        if (intermediateDataset.getValue().isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.asList(((Dataset<Row>) intermediateDataset.getValue().get()).columns());
    }
}
