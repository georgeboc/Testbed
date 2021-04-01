package com.testbed.boundary.invocations.spark;

import com.clearspring.analytics.util.Preconditions;
import com.testbed.boundary.invocations.InvocationParameters;
import com.testbed.boundary.invocations.Invokable;
import com.testbed.boundary.invocations.results.Result;
import com.testbed.boundary.invocations.results.SparkResult;
import com.testbed.entities.exceptions.TolerableErrorPercentageExceeded;
import com.testbed.entities.operations.physical.PhysicalProject;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import static java.lang.Math.abs;

public class ProjectInvokable implements Invokable {
    @Override
    public Result invoke(InvocationParameters invocationParameters) {
        Preconditions.checkArgument(invocationParameters.getInputResults().size() == 1,
                "Project operation is receiving %d inputs, although it is expected to receive one",
                invocationParameters.getInputResults().size());

        PhysicalProject physicalProject = (PhysicalProject) invocationParameters.getPhysicalOperation();
        Dataset<Row> outputDataset = getOutputDataset(invocationParameters, physicalProject);
        checkIfErrorIsTolerable(outputDataset.columns().length,
                physicalProject.getExpectedOutputColumnsCount(),
                invocationParameters.getTolerableErrorPercentage());
        return new SparkResult(outputDataset);
    }

    private Dataset<Row> getOutputDataset(InvocationParameters invocationParameters, PhysicalProject physicalProject) {
        Result inputResult = invocationParameters.getInputResults().stream().findFirst().get();
        Dataset<Row> inputDataset = (Dataset<Row>) inputResult.getValues();
        Column[] projectedColumns = (Column[]) physicalProject.getProjectedColumnNames().stream()
                .map(Column::new)
                .toArray();
        return inputDataset.select(projectedColumns);
    }

    private void checkIfErrorIsTolerable(final long columnsCount,
                                         final long expectedOutputColumnsCount,
                                         final double tolerableErrorPercentage) {
        double errorPercentage = abs(columnsCount - expectedOutputColumnsCount)*100/(double)expectedOutputColumnsCount;
        if (errorPercentage > tolerableErrorPercentage) {
            throw new TolerableErrorPercentageExceeded(errorPercentage, tolerableErrorPercentage);
        }
    }
}
