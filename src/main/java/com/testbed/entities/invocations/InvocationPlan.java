package com.testbed.entities.invocations;

import lombok.Data;

import java.util.List;

@Data
public class InvocationPlan {
    private final List<OperationInvocation> operationInvocations;
}
