package com.testbed.boundary.invocations.frameworks.spark;

import com.testbed.boundary.invocations.InvocationParameters;
import com.testbed.boundary.invocations.Operation;
import com.testbed.boundary.invocations.intermediateDatasets.IntermediateDataset;
import com.testbed.boundary.invocations.intermediateDatasets.SparkIntermediateDataset;
import com.testbed.entities.operations.physical.PhysicalLoad;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import static com.testbed.boundary.invocations.OperationsConstants.LOAD;

@RequiredArgsConstructor
public class LoadSparkOperation implements Operation {
    private static final String PARSED_DATASET_FILENAME = "parsed_dataset";
    private final SparkSession sparkSession;
    @Getter
    private final String name = LOAD;

    @Override
    public IntermediateDataset invoke(final InvocationParameters invocationParameters) {
        PhysicalLoad physicalLoad = (PhysicalLoad) invocationParameters.getPhysicalOperation();
        Dataset<Row> dataset = sparkSession.read().parquet(physicalLoad.getDatasetDirectoryPath() + '/' +
                PARSED_DATASET_FILENAME);
        return new SparkIntermediateDataset(dataset);
    }
}
