package com.testbed.interactors.jobs;

import com.clearspring.analytics.util.Lists;
import com.google.common.collect.Streams;
import com.testbed.boundary.invocations.InvocationParameters;
import com.testbed.boundary.invocations.Invokable;
import com.testbed.boundary.invocations.JobOperationInvocation;
import com.testbed.boundary.invocations.results.Result;
import com.testbed.entities.jobs.Job;
import com.testbed.entities.jobs.JobOperation;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@RequiredArgsConstructor
@SuppressWarnings("UnstableApiUsage")
public class JobInvoker {
    private final Map<String, Invokable> physicalOperationToInstrumentedInvocationMapper;

    public void invokeJob(Job job) {
        Stream<Invokable> invokableStream = getInvokablesStream(job.getJobOperations());
        Stream<JobOperation> jobOperationStream = job.getJobOperations().stream();
        Stream<JobOperationInvocation> jobOperationInvocationStream = Streams.zip(invokableStream,
                jobOperationStream,
                JobOperationInvocation::new);
        Stack<Result> resultStack = new Stack<>();
        jobOperationInvocationStream.forEach(jobOperationInvocation -> invokeJobOperation(jobOperationInvocation,
                resultStack));
    }

    private Stream<Invokable> getInvokablesStream(List<JobOperation> jobOperations) {
        return jobOperations.stream()
                .map(jobOperation -> jobOperation.getPhysicalOperation().getClass().getSimpleName())
                .map(physicalOperationToInstrumentedInvocationMapper::get);
    }

    private void invokeJobOperation(JobOperationInvocation jobOperationInvocation, Stack<Result> resultStack) {
        JobOperation jobOperation = jobOperationInvocation.getJobOperation();
        InvocationParameters invocationParameters = createInvocationParameters(jobOperation, resultStack);
        Result result = jobOperationInvocation.getInvokable().invoke(invocationParameters);
        IntStream.range(0, jobOperation.getSucceedingPhysicalOperationsCount())
                .forEach(unusedParam -> resultStack.push(result));
    }

    private InvocationParameters createInvocationParameters(JobOperation jobOperation, Stack<Result> resultStack) {
        return InvocationParameters.builder()
                .physicalOperation(jobOperation.getPhysicalOperation())
                .inputResults(getInputResults(jobOperation.getPrecedingPhysicalOperationsCount(), resultStack))
                .build();
    }

    private List<Result> getInputResults(int successivePhysicalOperationsCount, Stack<Result> resultStack) {
        List<Result> results = Lists.newArrayList();
        IntStream.range(0, successivePhysicalOperationsCount).forEach(unusedParam -> results.add(resultStack.pop()));
        return results;
    }
}
