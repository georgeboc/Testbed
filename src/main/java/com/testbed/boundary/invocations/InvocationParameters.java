package com.testbed.boundary.invocations;

import com.testbed.boundary.invocations.results.Result;
import com.testbed.entities.operations.physical.PhysicalOperation;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class InvocationParameters {
    private final List<Result> inputResults;
    private final PhysicalOperation physicalOperation;
}

