package com.testbed.boundary.invocations.spark;

import com.google.common.collect.Lists;
import com.testbed.boundary.invocations.InvocationParameters;
import com.testbed.boundary.invocations.Invokable;
import com.testbed.boundary.invocations.results.Result;
import com.testbed.boundary.invocations.results.SparkResult;
import com.testbed.entities.operations.physical.PhysicalJoin;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import scala.collection.JavaConversions;

import java.util.List;
import java.util.stream.Collectors;

public class JoinSparkInvokable implements Invokable {
    @Override
    public Result invoke(final InvocationParameters invocationParameters) {
        PhysicalJoin physicalJoin = (PhysicalJoin) invocationParameters.getPhysicalOperation();
        List<Dataset<Row>> inputDatasets = getInputDatasets(invocationParameters);
        Dataset<Row> outputDataset = getOutputDataset(inputDatasets, physicalJoin);
        return new SparkResult(outputDataset);
    }

    private List<Dataset<Row>> getInputDatasets(final InvocationParameters invocationParameters) {
        List<Result> inputResults = invocationParameters.getInputResults();
        return inputResults.stream()
                .map(Result::getValues)
                .map(object -> (Dataset<Row>) object)
                .collect(Collectors.toList());
    }

    private Dataset<Row> getOutputDataset(final List<Dataset<Row>> inputDatasets, final PhysicalJoin physicalJoin) {
        Dataset<Row> leftInputDataset = inputDatasets.get(0);
        Dataset<Row> rightInputDataset = inputDatasets.get(1);
        List<String> joinColumnNames = Lists.newArrayList(physicalJoin.getJoinLeftColumnName(), physicalJoin.getJoinRightColumnName());
        return leftInputDataset.join(rightInputDataset, JavaConversions.asScalaBuffer(joinColumnNames).toList());
    }
}
