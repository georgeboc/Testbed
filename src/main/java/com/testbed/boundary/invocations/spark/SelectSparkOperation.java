package com.testbed.boundary.invocations.spark;

import com.testbed.boundary.invocations.InvocationParameters;
import com.testbed.boundary.invocations.Invokable;
import com.testbed.boundary.invocations.Nameable;
import com.testbed.boundary.invocations.intermediateDatasets.IntermediateDataset;
import com.testbed.boundary.invocations.intermediateDatasets.SparkIntermediateDataset;
import com.testbed.entities.exceptions.TolerableErrorPercentageExceeded;
import com.testbed.entities.operations.physical.PhysicalSelect;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import static com.testbed.boundary.invocations.OperationsConstants.SELECT;
import static java.lang.Math.abs;

@RequiredArgsConstructor
public class SelectSparkOperation implements Invokable, Nameable {
    @Getter
    private final String name = SELECT;

    @Override
    public IntermediateDataset invoke(final InvocationParameters invocationParameters) {
        PhysicalSelect physicalSelect = (PhysicalSelect) invocationParameters.getPhysicalOperation();
        Dataset<Row> outputDataset = getOutputDataset(invocationParameters, physicalSelect);
        checkIfErrorIsTolerable(outputDataset.count(),
                physicalSelect.getExpectedOutputRowsCount(),
                invocationParameters.getTolerableErrorPercentage());
        return new SparkIntermediateDataset(outputDataset);
    }

    private Dataset<Row> getOutputDataset(final InvocationParameters invocationParameters,
                                          final PhysicalSelect physicalSelect) {
        IntermediateDataset inputIntermediateDataset = invocationParameters.getInputIntermediateDatasets().stream().findFirst().get();
        Dataset<Row> inputDataset = (Dataset<Row>) inputIntermediateDataset.getValue();
        return inputDataset.filter(physicalSelect.getColumnName() + " <= '" + physicalSelect.getLessThanValue() + "'");
    }

    private void checkIfErrorIsTolerable(final long rowCount,
                                         final long expectedOutputRowsCount,
                                         final double tolerableErrorPercentage) {
        double errorPercentage = abs(rowCount - expectedOutputRowsCount)*100/(double)expectedOutputRowsCount;
        if (errorPercentage > tolerableErrorPercentage) {
            throw new TolerableErrorPercentageExceeded(errorPercentage, tolerableErrorPercentage);
        }
    }
}
