package com.testbed.boundary.invocations.instrumentation;

import com.testbed.boundary.invocations.InvocationParameters;
import com.testbed.boundary.invocations.Operation;
import com.testbed.boundary.invocations.intermediateDatasets.IntermediateDataset;
import com.testbed.boundary.invocations.intermediateDatasets.instrumentation.IntermediateDatasetInstrumentation;
import com.testbed.entities.exceptions.TolerableErrorPercentageExceeded;
import com.testbed.entities.operations.physical.PhysicalSelect;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.testbed.boundary.invocations.OperationsConstants.SELECT;
import static java.lang.Math.abs;

@RequiredArgsConstructor
public class SelectOperationTolerableErrorCheck implements Operation {
    private final Operation wrappedOperation;
    private final IntermediateDatasetInstrumentation intermediateDatasetInstrumentation;

    @Override
    public IntermediateDataset invoke(InvocationParameters invocationParameters) {
        IntermediateDataset outputDataset = wrappedOperation.invoke(invocationParameters);
        PhysicalSelect physicalSelect = (PhysicalSelect) invocationParameters.getPhysicalOperation();
        checkIfErrorIsTolerable(intermediateDatasetInstrumentation.count(outputDataset),
                physicalSelect.getApproximatedOutputRowsCount(),
                invocationParameters.getTolerableErrorPercentage());
        return outputDataset;
    }

    private void checkIfErrorIsTolerable(final long realRowsCount,
                                         final long approximatedOutputRowsCount,
                                         final double tolerableErrorPercentage) {
        double errorPercentage = abs(realRowsCount - approximatedOutputRowsCount)*100/(double)realRowsCount;
        if (errorPercentage > tolerableErrorPercentage) {
            throw new TolerableErrorPercentageExceeded(errorPercentage, tolerableErrorPercentage);
        }
    }

    @Override
    public String getName() {
        return wrappedOperation.getName();
    }
}
