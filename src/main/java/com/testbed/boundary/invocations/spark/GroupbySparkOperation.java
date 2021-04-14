package com.testbed.boundary.invocations.spark;

import com.google.common.collect.Maps;
import com.testbed.boundary.invocations.InvocationParameters;
import com.testbed.boundary.invocations.Operation;
import com.testbed.boundary.invocations.intermediateDatasets.IntermediateDataset;
import com.testbed.boundary.invocations.intermediateDatasets.SparkIntermediateDataset;
import com.testbed.entities.operations.physical.PhysicalGroupBy;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import scala.collection.JavaConverters;
import scala.collection.Seq;

import java.util.List;
import java.util.stream.Collectors;

import static com.testbed.boundary.invocations.OperationsConstants.GROUP_BY;

@RequiredArgsConstructor
public class GroupbySparkOperation implements Operation {
    @Getter
    private final String name = GROUP_BY;

    @Override
    public IntermediateDataset invoke(final InvocationParameters invocationParameters) {
        IntermediateDataset inputIntermediateDataset = invocationParameters.getInputIntermediateDatasets().stream().findFirst().get();
        Dataset<Row> inputDataset = (Dataset<Row>) inputIntermediateDataset.getValue().get();
        PhysicalGroupBy physicalGroupBy = (PhysicalGroupBy) invocationParameters.getPhysicalOperation();
        List<Column> groupByColumns =  physicalGroupBy.getGroupingColumnNames().stream()
                .map(Column::new)
                .collect(Collectors.toList());
        Seq<Column> groupByColumnsSeq = JavaConverters.asScalaIteratorConverter(groupByColumns.iterator())
                .asScala()
                .toSeq();
        Dataset<Row> groupedDataset = inputDataset.groupBy(groupByColumnsSeq).agg(Maps.newHashMap());
        return new SparkIntermediateDataset(groupedDataset);
    }
}
