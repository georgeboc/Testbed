package com.testbed.boundary.invocations.spark;

import com.google.common.base.CaseFormat;
import com.testbed.boundary.invocations.InvocationParameters;
import com.testbed.boundary.invocations.Operation;
import com.testbed.boundary.invocations.intermediateDatasets.IntermediateDataset;
import com.testbed.boundary.invocations.intermediateDatasets.SparkIntermediateDataset;
import com.testbed.entities.operations.physical.PhysicalAggregate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import static com.testbed.boundary.invocations.OperationsConstants.AGGREGATE;
import static org.apache.spark.sql.functions.expr;

@RequiredArgsConstructor
public class AggregateSparkOperation implements Operation {
    @Getter
    private final String name = AGGREGATE;

    @Override
    public IntermediateDataset invoke(final InvocationParameters invocationParameters) {
        PhysicalAggregate physicalAggregate = (PhysicalAggregate) invocationParameters.getPhysicalOperation();
        Dataset<Row> outputDataset = getOutputDataset(invocationParameters, physicalAggregate);
        return new SparkIntermediateDataset(outputDataset);
    }

    private Dataset<Row> getOutputDataset(final InvocationParameters invocationParameters,
                                          final PhysicalAggregate physicalAggregate) {
        IntermediateDataset inputIntermediateDataset = invocationParameters.getInputIntermediateDatasets().stream().findFirst().get();
        Dataset<Row> inputDataset = (Dataset<Row>) inputIntermediateDataset.getValue().get();
        String operationExpression = String.format("%s(%s)",
                physicalAggregate.getAggregationOperation(),
                physicalAggregate.getAggregationColumnName());
        Dataset<Row> aggregatedDataset = inputDataset.agg(expr(operationExpression));
        String operationName = CaseFormat.LOWER_HYPHEN.to(CaseFormat.UPPER_CAMEL,
                physicalAggregate.getAggregationOperation());
        String operationColumnName = operationName + physicalAggregate.getAggregationColumnName();
        return aggregatedDataset.withColumnRenamed(operationExpression, operationColumnName);
    }
}
