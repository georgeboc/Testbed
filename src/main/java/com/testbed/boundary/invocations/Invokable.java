package com.testbed.boundary.invocations;

import com.testbed.boundary.invocations.results.Result;

public interface Invokable {
    Result invoke(final InvocationParameters invocationParameters);
}
