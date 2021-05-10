package com.testbed.boundary.invocations.frameworks.spark;

import com.testbed.boundary.invocations.InvocationParameters;
import com.testbed.boundary.invocations.Operation;
import com.testbed.boundary.invocations.intermediateDatasets.IntermediateDataset;
import com.testbed.boundary.invocations.intermediateDatasets.NoIntermediateDataset;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.io.IOException;

import static com.testbed.boundary.invocations.OperationsConstants.SINK;

@RequiredArgsConstructor
public class SinkSparkOperation implements Operation {
    public static final String SPARK_EXECUTION_PREFIX = ".spark_execution/";
    private static final String PARQUET = "parquet";
    private static final boolean DELETE_RECURSIVELY = true;

    private final FileSystem fileSystem;

    @Getter
    private final String name = SINK;

    @Override
    public IntermediateDataset invoke(final InvocationParameters invocationParameters) {
        IntermediateDataset inputIntermediateDataset = invocationParameters.getInputIntermediateDatasets().stream().findFirst().get();
        Dataset<Row> inputDataset = (Dataset<Row>) inputIntermediateDataset.getValue().get();
        tryDeleteSparkExecutionDirectory();
        inputDataset.write().format(PARQUET).save(SPARK_EXECUTION_PREFIX + invocationParameters.getPhysicalOperation().getId());
        return new NoIntermediateDataset();
    }

    private void tryDeleteSparkExecutionDirectory() {
        try {
            fileSystem.delete(new Path(SPARK_EXECUTION_PREFIX), DELETE_RECURSIVELY);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
