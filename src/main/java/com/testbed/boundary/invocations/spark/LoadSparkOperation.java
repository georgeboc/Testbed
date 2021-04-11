package com.testbed.boundary.invocations.spark;

import com.testbed.boundary.invocations.InvocationParameters;
import com.testbed.boundary.invocations.Invokable;
import com.testbed.boundary.invocations.Nameable;
import com.testbed.boundary.invocations.intermediateDatasets.IntermediateDataset;
import com.testbed.boundary.invocations.intermediateDatasets.SparkIntermediateDataset;
import com.testbed.entities.operations.physical.PhysicalLoad;
import lombok.RequiredArgsConstructor;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

@RequiredArgsConstructor
public class LoadSparkOperation implements Invokable, Nameable {
    private static final String PARSED_DATASET_FILENAME = "parsed_dataset.parquet";
    private static final String LOAD = "Load";
    private final SparkSession sparkSession;

    @Override
    public IntermediateDataset invoke(final InvocationParameters invocationParameters) {
        PhysicalLoad physicalLoad = (PhysicalLoad) invocationParameters.getPhysicalOperation();
        Dataset<Row> dataset = sparkSession.read().parquet(physicalLoad.getDatasetDirectoryPath() + '/' +
                PARSED_DATASET_FILENAME);
        return new SparkIntermediateDataset(dataset);
    }

    @Override
    public String getName() {
        return LOAD;
    }
}
