package com.testbed.boundary.invocations.spark;

import com.testbed.boundary.invocations.InvocationParameters;
import com.testbed.boundary.invocations.Invokable;
import com.testbed.boundary.invocations.intermediateDatasets.IntermediateDataset;
import com.testbed.boundary.invocations.intermediateDatasets.SparkIntermediateDataset;
import com.testbed.entities.exceptions.TolerableErrorPercentageExceeded;
import com.testbed.entities.operations.physical.PhysicalProject;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import scala.collection.JavaConverters;
import scala.collection.Seq;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.abs;

public class ProjectSparkInvokable implements Invokable {
    @Override
    public IntermediateDataset invoke(final InvocationParameters invocationParameters) {
        PhysicalProject physicalProject = (PhysicalProject) invocationParameters.getPhysicalOperation();
        Dataset<Row> inputDataset = getInputDataset(invocationParameters);
        Dataset<Row> outputDataset = getOutputDataset(inputDataset, physicalProject);
        checkIfErrorIsTolerable(inputDataset.columns().length,
                outputDataset.columns().length,
                physicalProject.getExpectedColumnsSelectionFactor(),
                invocationParameters.getTolerableErrorPercentage());
        return new SparkIntermediateDataset(outputDataset);
    }

    private Dataset<Row> getInputDataset(final InvocationParameters invocationParameters) {
        IntermediateDataset inputIntermediateDataset = invocationParameters.getInputIntermediateDatasets().stream().findFirst().get();
        return (Dataset<Row>) inputIntermediateDataset.getValue();
    }

    private Dataset<Row> getOutputDataset(final Dataset<Row> inputDataset, final PhysicalProject physicalProject) {
        List<Column> projectedColumns = physicalProject.getProjectedColumnNames().stream()
                .map(Column::new)
                .collect(Collectors.toList());
        Seq<Column> projectedColumnsSeq = JavaConverters.asScalaIteratorConverter(projectedColumns.iterator())
                .asScala()
                .toSeq();
        return inputDataset.select(projectedColumnsSeq);
    }

    private void checkIfErrorIsTolerable(final long inputColumnsCount,
                                         final long outputColumnsCount,
                                         final double expectedColumnsSelectionFactor,
                                         final double tolerableErrorPercentage) {
        double errorPercentage = abs((double)outputColumnsCount/inputColumnsCount - expectedColumnsSelectionFactor)*100;
        if (errorPercentage > tolerableErrorPercentage) {
            throw new TolerableErrorPercentageExceeded(errorPercentage, tolerableErrorPercentage);
        }
    }
}
