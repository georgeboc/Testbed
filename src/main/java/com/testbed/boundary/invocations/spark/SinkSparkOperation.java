package com.testbed.boundary.invocations.spark;

import com.testbed.boundary.invocations.InvocationParameters;
import com.testbed.boundary.invocations.Operation;
import com.testbed.boundary.invocations.intermediateDatasets.IntermediateDataset;
import com.testbed.boundary.invocations.intermediateDatasets.NoIntermediateDataset;
import com.testbed.boundary.invocations.intermediateDatasets.instrumentation.IntermediateDatasetInstrumentation;
import com.testbed.boundary.invocations.intermediateDatasets.instrumentation.SparkIntermediateDatasetInstrumentation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import static com.testbed.boundary.invocations.OperationsConstants.SINK;

@RequiredArgsConstructor
public class SinkSparkOperation implements Operation {
    @Getter
    private final String name = SINK;

    @Override
    public IntermediateDataset invoke(final InvocationParameters invocationParameters) {
        IntermediateDataset inputIntermediateDataset = invocationParameters.getInputIntermediateDatasets().stream().findFirst().get();
        Dataset<Row> inputDataset = (Dataset<Row>) inputIntermediateDataset.getValue().get();
        inputDataset.count();
        return new NoIntermediateDataset();
    }
}
