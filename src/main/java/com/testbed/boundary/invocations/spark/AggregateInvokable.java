package com.testbed.boundary.invocations.spark;

import com.clearspring.analytics.util.Preconditions;
import com.testbed.boundary.invocations.InvocationParameters;
import com.testbed.boundary.invocations.Invokable;
import com.testbed.boundary.invocations.results.Result;
import com.testbed.boundary.invocations.results.SparkResult;
import com.testbed.entities.operations.physical.PhysicalAggregate;
import lombok.RequiredArgsConstructor;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import static org.apache.spark.sql.functions.expr;

@RequiredArgsConstructor
public class AggregateInvokable implements Invokable {
    @Override
    public Result invoke(final InvocationParameters invocationParameters) {
        Preconditions.checkArgument(invocationParameters.getInputResults().size() == 1,
                "Aggregation operation is receiving %d inputs, although it is expected to receive one",
                invocationParameters.getInputResults().size());

        PhysicalAggregate physicalAggregate = (PhysicalAggregate) invocationParameters.getPhysicalOperation();
        Dataset<Row> outputDataset = getOutputDataset(invocationParameters, physicalAggregate);
        return new SparkResult(outputDataset);
    }

    private Dataset<Row> getOutputDataset(InvocationParameters invocationParameters, PhysicalAggregate physicalAggregate) {
        Result inputResult = invocationParameters.getInputResults().stream().findFirst().get();
        Dataset<Row> inputDataset = (Dataset<Row>) inputResult.getValues();
        return inputDataset.agg(expr(String.format("%s(%s)",
                physicalAggregate.getAggregationOperation(),
                physicalAggregate.getAggregationColumnName())));
    }
}
