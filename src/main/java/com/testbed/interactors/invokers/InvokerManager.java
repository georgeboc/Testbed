package com.testbed.interactors.invokers;

import com.clearspring.analytics.util.Lists;
import com.google.common.collect.Streams;
import com.testbed.boundary.invocations.InvocationParameters;
import com.testbed.boundary.invocations.Invokable;
import com.testbed.boundary.invocations.results.Result;
import com.testbed.entities.invocations.InvocationPlan;
import com.testbed.entities.invocations.OperationInvocation;
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
public class InvokerManager {
    @Inject
    private ApplicationContext applicationContext;

    public void invoke(final InvocationPlan invocationPlan, final double tolerableErrorPercentage) {
        Stream<Invokable> invokableStream = getInvokableStream(invocationPlan.getOperationInvocations());
        Stream<OperationInvocation> operationInvocationStream = invocationPlan.getOperationInvocations().stream();
        Stack<Result> resultStack = new Stack<>();
        Streams.forEachPair(invokableStream,
                operationInvocationStream,
                (invokable, operationInvocation) -> invokeOperation(invokable,
                        operationInvocation,
                        resultStack,
                        tolerableErrorPercentage));
    }

    private Stream<Invokable> getInvokableStream(final List<OperationInvocation> operationInvocations) {
        return operationInvocations.stream().map(this::getInvokable);
    }

    private Invokable getInvokable(final OperationInvocation operationInvocation) {
        return BeanFactoryAnnotationUtils.qualifiedBeanOfType(applicationContext.getAutowireCapableBeanFactory(),
                Invokable.class, operationInvocation.getPhysicalOperation().getClass().getSimpleName());
    }

    private void invokeOperation(final Invokable invokable,
                                 final OperationInvocation operationInvocation,
                                 final Stack<Result> resultStack,
                                 final double tolerableErrorPercentage) {
        InvocationParameters invocationParameters = createInvocationParameters(operationInvocation,
                resultStack,
                tolerableErrorPercentage);
        Result result = invokable.invoke(invocationParameters);
        IntStream.range(0, operationInvocation.getSucceedingPhysicalOperationsCount())
                .forEach(unusedParam -> resultStack.push(result));
    }

    private InvocationParameters createInvocationParameters(final OperationInvocation operationInvocation,
                                                            final Stack<Result> resultStack,
                                                            final double tolerableErrorPercentage) {
        return InvocationParameters.builder()
                .physicalOperation(operationInvocation.getPhysicalOperation())
                .inputResults(getInputResults(operationInvocation.getPrecedingPhysicalOperationsCount(), resultStack))
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
