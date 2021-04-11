package com.testbed.boundary.invocations.spark;

import com.google.common.collect.Maps;
import com.testbed.boundary.invocations.InvocationParameters;
import com.testbed.boundary.invocations.Invokable;
import com.testbed.boundary.invocations.Nameable;
import com.testbed.boundary.invocations.intermediateDatasets.IntermediateDataset;
import com.testbed.boundary.invocations.intermediateDatasets.SparkIntermediateDataset;
import com.testbed.entities.operations.physical.PhysicalGroupby;
import lombok.RequiredArgsConstructor;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import scala.collection.JavaConverters;
import scala.collection.Seq;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GroupbySparkOperation implements Invokable, Nameable {
    private static final String GROUP_BY = "Group By";

    @Override
    public IntermediateDataset invoke(final InvocationParameters invocationParameters) {
        IntermediateDataset inputIntermediateDataset = invocationParameters.getInputIntermediateDatasets().stream().findFirst().get();
        Dataset<Row> inputDataset = (Dataset<Row>) inputIntermediateDataset.getValue();
        PhysicalGroupby physicalGroupBy = (PhysicalGroupby) invocationParameters.getPhysicalOperation();
        List<Column> groupByColumns =  physicalGroupBy.getGroupingColumnNames().stream()
                .map(Column::new)
                .collect(Collectors.toList());
        Seq<Column> groupByColumnsSeq = JavaConverters.asScalaIteratorConverter(groupByColumns.iterator())
                .asScala()
                .toSeq();
        Dataset<Row> groupedDataset = inputDataset.groupBy(groupByColumnsSeq).agg(Maps.newHashMap());
        return new SparkIntermediateDataset(groupedDataset);
    }

    @Override
    public String getName() {
        return GROUP_BY;
    }
}
