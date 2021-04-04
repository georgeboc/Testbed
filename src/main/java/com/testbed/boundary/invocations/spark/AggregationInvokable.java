package com.testbed.boundary.invocations.spark;

import com.clearspring.analytics.util.Preconditions;
import com.testbed.boundary.invocations.InvocationParameters;
import com.testbed.boundary.invocations.Invokable;
import com.testbed.boundary.invocations.results.Result;
import com.testbed.boundary.invocations.results.SparkResult;
import com.testbed.entities.operations.physical.PhysicalAggregation;
import lombok.RequiredArgsConstructor;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import static org.apache.spark.sql.functions.expr;

@RequiredArgsConstructor
public class AggregationInvokable implements Invokable {
    @Override
    public Result invoke(final InvocationParameters invocationParameters) {
        Preconditions.checkArgument(invocationParameters.getInputResults().size() == 1,
                "Aggregation operation is receiving %d inputs, although it is expected to receive one",
                invocationParameters.getInputResults().size());

        PhysicalAggregation physicalAggregation = (PhysicalAggregation) invocationParameters.getPhysicalOperation();
        Dataset<Row> outputDataset = getOutputDataset(invocationParameters, physicalAggregation);
        return new SparkResult(outputDataset);
    }

    private Dataset<Row> getOutputDataset(InvocationParameters invocationParameters, PhysicalAggregation physicalAggregation) {
        Result inputResult = invocationParameters.getInputResults().stream().findFirst().get();
        Dataset<Row> inputDataset = (Dataset<Row>) inputResult.getValues();
        return inputDataset.agg(expr(String.format("%s(%s)",
                physicalAggregation.getOperation(),
                physicalAggregation.getAggregationColumnName())));
    }
}
