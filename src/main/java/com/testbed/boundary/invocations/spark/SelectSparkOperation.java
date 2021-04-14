package com.testbed.boundary.invocations.spark;

import com.testbed.boundary.invocations.InvocationParameters;
import com.testbed.boundary.invocations.Operation;
import com.testbed.boundary.invocations.intermediateDatasets.IntermediateDataset;
import com.testbed.boundary.invocations.intermediateDatasets.SparkIntermediateDataset;
import com.testbed.entities.operations.physical.PhysicalSelect;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import scala.Tuple2;

import java.util.Arrays;

import static com.testbed.boundary.invocations.OperationsConstants.SELECT;

@RequiredArgsConstructor
public class SelectSparkOperation implements Operation {
    private static final String TIMESTAMP_TYPE = "TimestampType";
    @Getter
    private final String name = SELECT;

    @Override
    public IntermediateDataset invoke(final InvocationParameters invocationParameters) {
        PhysicalSelect physicalSelect = (PhysicalSelect) invocationParameters.getPhysicalOperation();
        Dataset<Row> outputDataset = getOutputDataset(invocationParameters, physicalSelect);
        return new SparkIntermediateDataset(outputDataset);
    }

    private Dataset<Row> getOutputDataset(final InvocationParameters invocationParameters,
                                          final PhysicalSelect physicalSelect) {
        IntermediateDataset inputIntermediateDataset = invocationParameters.getInputIntermediateDatasets().stream().findFirst().get();
        Dataset<Row> inputDataset = (Dataset<Row>) inputIntermediateDataset.getValue().get();
        String filterQuery = getFilterQuery(inputDataset, physicalSelect);
        return inputDataset.filter(filterQuery);
    }

    private String getFilterQuery(final Dataset<Row> inputDataset, final PhysicalSelect physicalSelect) {
        String columnType = Arrays.stream(inputDataset.dtypes())
                .filter(columnNameAndType -> columnNameAndType._1().equals(physicalSelect.getColumnName()))
                .map(Tuple2::_2)
                .findFirst()
                .get();
        if (columnType.equals(TIMESTAMP_TYPE)) {
            return String.format("unix_timestamp(%s)*1000 <= %s",
                    physicalSelect.getColumnName(),
                    physicalSelect.getLessThanOrEqualValue());
        }
        return String.format("string(%s) <= '%s'", physicalSelect.getColumnName(), physicalSelect.getLessThanOrEqualValue());
    }
}
