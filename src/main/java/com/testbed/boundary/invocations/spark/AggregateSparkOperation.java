package com.testbed.boundary.invocations.spark;

import com.testbed.boundary.invocations.InvocationParameters;
import com.testbed.boundary.invocations.Invokable;
import com.testbed.boundary.invocations.Nameable;
import com.testbed.boundary.invocations.intermediateDatasets.IntermediateDataset;
import com.testbed.boundary.invocations.intermediateDatasets.SparkIntermediateDataset;
import com.testbed.entities.operations.physical.PhysicalAggregate;
import lombok.RequiredArgsConstructor;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import static org.apache.spark.sql.functions.expr;

@RequiredArgsConstructor
public class AggregateSparkOperation implements Invokable, Nameable {
    private static final String AGGREGATE = "Aggregate";

    @Override
    public IntermediateDataset invoke(final InvocationParameters invocationParameters) {
        PhysicalAggregate physicalAggregate = (PhysicalAggregate) invocationParameters.getPhysicalOperation();
        Dataset<Row> outputDataset = getOutputDataset(invocationParameters, physicalAggregate);
        return new SparkIntermediateDataset(outputDataset);
    }

    private Dataset<Row> getOutputDataset(final InvocationParameters invocationParameters,
                                          final PhysicalAggregate physicalAggregate) {
        IntermediateDataset inputIntermediateDataset = invocationParameters.getInputIntermediateDatasets().stream().findFirst().get();
        Dataset<Row> inputDataset = (Dataset<Row>) inputIntermediateDataset.getValue();
        return inputDataset.agg(expr(String.format("%s(%s)",
                physicalAggregate.getAggregationOperation(),
                physicalAggregate.getAggregationColumnName())));
    }

    @Override
    public String getName() {
        return AGGREGATE;
    }
}
