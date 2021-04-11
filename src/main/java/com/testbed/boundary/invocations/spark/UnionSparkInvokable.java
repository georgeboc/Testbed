package com.testbed.boundary.invocations.spark;

import com.testbed.boundary.invocations.InvocationParameters;
import com.testbed.boundary.invocations.Invokable;
import com.testbed.boundary.invocations.intermediateDatasets.IntermediateDataset;
import com.testbed.boundary.invocations.intermediateDatasets.SparkIntermediateDataset;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.List;
import java.util.stream.Collectors;

public class UnionSparkInvokable implements Invokable {
    @Override
    public IntermediateDataset invoke(final InvocationParameters invocationParameters) {
        List<Dataset<Row>> inputDatasets = getInputDatasets(invocationParameters);
        Dataset<Row> outputDataset = getOutputDataset(inputDatasets);
        return new SparkIntermediateDataset(outputDataset);
    }

    private List<Dataset<Row>> getInputDatasets(final InvocationParameters invocationParameters) {
        List<IntermediateDataset> inputIntermediateDatasets = invocationParameters.getInputIntermediateDatasets();
        return inputIntermediateDatasets.stream()
                .map(IntermediateDataset::getValue)
                .map(object -> (Dataset<Row>) object)
                .collect(Collectors.toList());
    }

    private Dataset<Row> getOutputDataset(final List<Dataset<Row>> inputDatasets) {
        Dataset<Row> leftInputDataset = inputDatasets.get(0);
        Dataset<Row> rightInputDataset = inputDatasets.get(1);
        return leftInputDataset.union(rightInputDataset);
    }
}
