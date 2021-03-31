package com.testbed.boundary.invocations;

import com.testbed.entities.jobs.JobOperation;
import lombok.Data;

@Data
public class JobOperationInvocation {
    private final Invokable invokable;
    private final JobOperation jobOperation;
}
