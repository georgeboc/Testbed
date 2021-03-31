package com.testbed.boundary.invocations.spark;

import com.clearspring.analytics.util.Preconditions;
import com.testbed.boundary.invocations.Invokable;
import com.testbed.boundary.invocations.InvocationParameters;
import com.testbed.boundary.invocations.results.Result;
import com.testbed.boundary.invocations.results.SparkResult;
import com.testbed.entities.operations.physical.PhysicalSelect;
import lombok.RequiredArgsConstructor;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

@RequiredArgsConstructor
public class SelectInvokable implements Invokable {
    @Override
    public Result invoke(final InvocationParameters invocationParameters) {
        Preconditions.checkArgument(invocationParameters.getInputResults().size() == 1,
                "Select operation receives %d inputs from previous node, when it is expected to receive one",
                invocationParameters.getInputResults().size());

        PhysicalSelect physicalSelect = (PhysicalSelect) invocationParameters.getPhysicalOperation();
        Result inputResult = invocationParameters.getInputResults().stream().findFirst().get();
        Dataset<Row> inputDataset = (Dataset<Row>) inputResult.getValues();
        Dataset<Row> outputDataset = inputDataset.filter(physicalSelect.getColumnName() + " <= '" + physicalSelect.getLessThanValue() + "'");
        return new SparkResult(outputDataset);
    }
}
