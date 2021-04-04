package com.testbed.boundary.invocations.spark;

import com.clearspring.analytics.util.Preconditions;
import com.testbed.boundary.invocations.InvocationParameters;
import com.testbed.boundary.invocations.Invokable;
import com.testbed.boundary.invocations.results.Result;
import com.testbed.boundary.invocations.results.SparkResult;
import com.testbed.entities.operations.physical.PhysicalUnion;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.List;
import java.util.stream.Collectors;

public class UnionInvokable implements Invokable {
    @Override
    public Result invoke(InvocationParameters invocationParameters) {
        Preconditions.checkArgument(invocationParameters.getInputResults().size() == 2,
                "Union operation is receiving %d inputs, although it is expected to receive two",
                invocationParameters.getInputResults().size());

        PhysicalUnion physicalUnion = (PhysicalUnion) invocationParameters.getPhysicalOperation();
        List<Dataset<Row>> inputDatasets = getInputDatasets(invocationParameters);
        Dataset<Row> outputDataset = getOutputDataset(inputDatasets, physicalUnion);
        return new SparkResult(outputDataset);
    }

    private List<Dataset<Row>> getInputDatasets(InvocationParameters invocationParameters) {
        List<Result> inputResults = invocationParameters.getInputResults();
        return inputResults.stream()
                .map(Result::getValues)
                .map(object -> (Dataset<Row>) object)
                .collect(Collectors.toList());
    }

    private Dataset<Row> getOutputDataset(List<Dataset<Row>> inputDatasets, PhysicalUnion physicalUnion) {
        Dataset<Row> leftInputDataset = inputDatasets.get(0);
        Dataset<Row> rightInputDataset = inputDatasets.get(1);
        return leftInputDataset.union(rightInputDataset);
    }
}
