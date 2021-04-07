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
import org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils;
import org.springframework.context.ApplicationContext;

import javax.inject.Inject;
import java.util.List;
import java.util.Stack;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@SuppressWarnings("UnstableApiUsage")
@RequiredArgsConstructor
public class JobInvoker {
    @Inject
    private ApplicationContext applicationContext;

    public void invokeJob(final Job job, final double tolerableErrorPercentage) {
        Stream<Invokable> invokableStream = getInvokablesStream(job.getJobOperations());
        Stream<JobOperation> jobOperationStream = job.getJobOperations().stream();
        Stack<Result> resultStack = new Stack<>();
        Streams.zip(invokableStream,
                jobOperationStream,
                JobOperationInvocation::new)
                .forEach(jobOperationInvocation -> invokeJobOperation(jobOperationInvocation,
                resultStack, tolerableErrorPercentage));
    }

    private Stream<Invokable> getInvokablesStream(final List<JobOperation> jobOperations) {
        return jobOperations.stream().map(this::getInvokable);
    }

    private Invokable getInvokable(JobOperation jobOperation) {
        return BeanFactoryAnnotationUtils.qualifiedBeanOfType(applicationContext.getAutowireCapableBeanFactory(),
                Invokable.class, jobOperation.getPhysicalOperation().getClass().getSimpleName());
    }

    private void invokeJobOperation(final JobOperationInvocation jobOperationInvocation,
                                    final Stack<Result> resultStack,
                                    final double tolerableErrorPercentage) {
        JobOperation jobOperation = jobOperationInvocation.getJobOperation();
        InvocationParameters invocationParameters = createInvocationParameters(jobOperation,
                resultStack,
                tolerableErrorPercentage);
        Result result = jobOperationInvocation.getInvokable().invoke(invocationParameters);
        IntStream.range(0, jobOperation.getSucceedingPhysicalOperationsCount())
                .forEach(unusedParam -> resultStack.push(result));
    }

    private InvocationParameters createInvocationParameters(final JobOperation jobOperation,
                                                            final Stack<Result> resultStack,
                                                            final double tolerableErrorPercentage) {
        return InvocationParameters.builder()
                .physicalOperation(jobOperation.getPhysicalOperation())
                .inputResults(getInputResults(jobOperation.getPrecedingPhysicalOperationsCount(), resultStack))
                .tolerableErrorPercentage(tolerableErrorPercentage)
                .build();
    }

    private List<Result> getInputResults(final int successivePhysicalOperationsCount,
                                         final Stack<Result> resultStack) {
        List<Result> results = Lists.newArrayList();
        IntStream.range(0, successivePhysicalOperationsCount).forEach(unusedParam -> results.add(resultStack.pop()));
        return results;
    }
}
