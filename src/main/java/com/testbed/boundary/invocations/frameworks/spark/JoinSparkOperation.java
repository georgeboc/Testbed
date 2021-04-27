package com.testbed.boundary.invocations.frameworks.spark;

import com.testbed.boundary.invocations.InvocationParameters;
import com.testbed.boundary.invocations.Operation;
import com.testbed.boundary.invocations.intermediateDatasets.IntermediateDataset;
import com.testbed.boundary.invocations.intermediateDatasets.SparkIntermediateDataset;
import com.testbed.entities.operations.physical.PhysicalJoin;
import lombok.Getter;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.testbed.boundary.invocations.OperationsConstants.JOIN;

public class JoinSparkOperation implements Operation {
    private static final String LEFT_PREFIX = "Left";
    private static final String RIGHT_PREFIX = "Right";
    private static final int LEFT_POSITION = 0;
    private static final int RIGHT_POSITION = 1;
    @Getter
    private final String name = JOIN;

    @Override
    public IntermediateDataset invoke(final InvocationParameters invocationParameters) {
        PhysicalJoin physicalJoin = (PhysicalJoin) invocationParameters.getPhysicalOperation();
        List<Dataset<Row>> inputDatasets = getInputDatasets(invocationParameters);
        Dataset<Row> outputDataset = getOutputDataset(inputDatasets, physicalJoin);
        return new SparkIntermediateDataset(outputDataset);
    }

    private List<Dataset<Row>> getInputDatasets(final InvocationParameters invocationParameters) {
        List<IntermediateDataset> inputIntermediateDatasets = invocationParameters.getInputIntermediateDatasets();
        return inputIntermediateDatasets.stream()
                .map(IntermediateDataset::getValue)
                .map(Optional::get)
                .map(object -> (Dataset<Row>) object)
                .collect(Collectors.toList());
    }

    private Dataset<Row> getOutputDataset(final List<Dataset<Row>> inputDatasets, final PhysicalJoin physicalJoin) {
        Dataset<Row> leftInputDataset = inputDatasets.get(LEFT_POSITION);
        Dataset<Row> leftInputDatasetRenamed = addPrefixToDataset(LEFT_PREFIX, leftInputDataset);
        Dataset<Row> rightInputDataset = inputDatasets.get(RIGHT_POSITION);
        Dataset<Row> rightInputDatasetRenamed = addPrefixToDataset(RIGHT_PREFIX, rightInputDataset);
        Column joinExpression = getJoinExpression(leftInputDatasetRenamed, rightInputDatasetRenamed, physicalJoin);
        return leftInputDatasetRenamed.join(rightInputDatasetRenamed, joinExpression);
    }

    private Column getJoinExpression(Dataset<Row> leftDataset, Dataset<Row> rightDataset, PhysicalJoin physicalJoin) {
        return leftDataset.col(LEFT_PREFIX + physicalJoin.getJoinLeftColumnName())
                .equalTo(rightDataset.col(RIGHT_PREFIX + physicalJoin.getJoinRightColumnName()));
    }

    private Dataset<Row> addPrefixToDataset(String prefix, Dataset<Row> dataset) {
        String[] columnNames = dataset.columns();
        Dataset<Row> newDataset = dataset;
        for (String columnName: columnNames) {
            newDataset = newDataset.withColumnRenamed(columnName, prefix + columnName);
        }
        return newDataset;
    }
}
