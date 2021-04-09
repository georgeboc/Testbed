package com.testbed.boundary.invocations.spark;

import com.google.common.collect.Maps;
import com.testbed.boundary.invocations.InvocationParameters;
import com.testbed.boundary.invocations.Invokable;
import com.testbed.boundary.invocations.results.Result;
import com.testbed.boundary.invocations.results.SparkResult;
import lombok.RequiredArgsConstructor;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import scala.collection.JavaConverters;
import scala.collection.Seq;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GroupbySparkInvokable implements Invokable {
    @Override
    public Result invoke(final InvocationParameters invocationParameters) {
        Result inputResult = invocationParameters.getInputResults().stream().findFirst().get();
        Dataset<Row> inputDataset = (Dataset<Row>) inputResult.getValues();
        List<Column> groupByColumns = Arrays.stream(inputDataset.columns())
                .map(Column::new)
                .collect(Collectors.toList());
        Seq<Column> groupByColumnsSeq = JavaConverters.asScalaIteratorConverter(groupByColumns.iterator())
                .asScala()
                .toSeq();
        Dataset<Row> resultDataset = inputDataset.groupBy(groupByColumnsSeq).agg(Maps.newHashMap());
        return new SparkResult(resultDataset);
    }
}
