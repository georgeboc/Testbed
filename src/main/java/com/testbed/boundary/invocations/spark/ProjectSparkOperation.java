package com.testbed.boundary.invocations.spark;

import com.testbed.boundary.invocations.InvocationParameters;
import com.testbed.boundary.invocations.Operation;
import com.testbed.boundary.invocations.intermediateDatasets.IntermediateDataset;
import com.testbed.boundary.invocations.intermediateDatasets.SparkIntermediateDataset;
import com.testbed.entities.operations.physical.PhysicalProject;
import lombok.Getter;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import scala.collection.JavaConverters;
import scala.collection.Seq;

import java.util.List;
import java.util.stream.Collectors;

import static com.testbed.boundary.invocations.OperationsConstants.PROJECT;

public class ProjectSparkOperation implements Operation {
    @Getter
    private final String name = PROJECT;

    @Override
    public IntermediateDataset invoke(final InvocationParameters invocationParameters) {
        PhysicalProject physicalProject = (PhysicalProject) invocationParameters.getPhysicalOperation();
        Dataset<Row> inputDataset = getInputDataset(invocationParameters);
        Dataset<Row> outputDataset = getOutputDataset(inputDataset, physicalProject);
        return new SparkIntermediateDataset(outputDataset);
    }

    private Dataset<Row> getInputDataset(final InvocationParameters invocationParameters) {
        IntermediateDataset inputIntermediateDataset = invocationParameters.getInputIntermediateDatasets().stream().findFirst().get();
        return (Dataset<Row>) inputIntermediateDataset.getValue().get();
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
}
