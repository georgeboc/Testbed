package com.testbed.boundary.invocations.spark;

import com.clearspring.analytics.util.Preconditions;
import com.testbed.boundary.invocations.Invokable;
import com.testbed.boundary.invocations.InvocationParameters;
import com.testbed.boundary.invocations.results.NoResult;
import com.testbed.boundary.invocations.results.Result;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SinkInvokable implements Invokable {
    @Override
    public Result invoke(final InvocationParameters invocationParameters) {
        Preconditions.checkArgument(invocationParameters.getInputResults().size() == 1,
                "Sink operation is expected to receive one input from a previous node, although it " +
                        "is receiving %d inputs", invocationParameters.getInputResults().size());

        Result inputResult = invocationParameters.getInputResults().stream().findFirst().get();
        inputResult.count();
        return new NoResult();
    }
}
