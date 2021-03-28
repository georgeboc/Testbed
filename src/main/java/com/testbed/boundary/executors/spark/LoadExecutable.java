package com.testbed.boundary.executors.spark;

import com.testbed.boundary.executors.Executable;
import com.testbed.boundary.executors.OperationInput;
import com.testbed.boundary.executors.Result;
import com.clearspring.analytics.util.Preconditions;
import com.testbed.entities.operations.physical.PhysicalLoad;
import lombok.RequiredArgsConstructor;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

@RequiredArgsConstructor
public class LoadExecutable implements Executable {
    private static final String PARSED_DATASET_FILENAME = "parsed_dataset.parquet";
    private final SparkSession sparkSession;

    @Override
    public Result execute(OperationInput operationInput) {
        Preconditions.checkArgument(operationInput.getInputResults().size() == 0, "Load operation receives %d inputs from previous node, when it is expected to receive none", operationInput.getInputResults().size());

        PhysicalLoad physicalLoad = (PhysicalLoad) operationInput.getPhysicalOperation();
        Dataset<Row> dataset = sparkSession.read().parquet(physicalLoad.getDatasetDirectoryPath() + '/' + PARSED_DATASET_FILENAME);
        return new SparkResult(dataset);
    }
}
