package com.testbed.boundary.executors.spark;

import com.clearspring.analytics.util.Preconditions;
import com.testbed.boundary.executors.Executable;
import com.testbed.boundary.executors.OperationInput;
import com.testbed.boundary.executors.Result;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class SinkExecutable implements Executable {
    @Override
    public Result execute(OperationInput operationInput) {
        Preconditions.checkArgument(operationInput.getInputResults().size() == 1, "Sink operation receives %d inputs from previous node, when it is expected to receive one", operationInput.getInputResults().size());

        List<Result> inputResults = new ArrayList<>(operationInput.getInputResults());
        Result inputResult = inputResults.get(0);
        inputResult.count();
        return new NoResult();
    }
}
