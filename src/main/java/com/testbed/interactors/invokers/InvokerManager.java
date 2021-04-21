package com.testbed.interactors.invokers;

import com.clearspring.analytics.util.Lists;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Streams;
import com.testbed.boundary.invocations.InvocationParameters;
import com.testbed.boundary.invocations.Invokable;
import com.testbed.boundary.invocations.intermediateDatasets.IntermediateDataset;
import com.testbed.entities.invocations.InvocationPlan;
import com.testbed.entities.invocations.OperationInvocation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils;
import org.springframework.context.ApplicationContext;

import javax.inject.Inject;
import java.util.List;
import java.util.Stack;
import java.util.stream.Stream;

@SuppressWarnings("UnstableApiUsage")
@RequiredArgsConstructor
public class InvokerManager {
    @Inject
    private ApplicationContext applicationContext;

    public Stopwatch invoke(final InvocationPlan invocationPlan, final double tolerableErrorPercentage) {
        Stream<Invokable> invokableStream = getInvokableStream(invocationPlan.getOperationInvocations());
        Stream<OperationInvocation> operationInvocationStream = invocationPlan.getOperationInvocations().stream();
        Stack<IntermediateDataset> intermediateDataset = new Stack<>();
        Stopwatch stopWatch = Stopwatch.createStarted();
        invokeOperations(tolerableErrorPercentage, invokableStream, operationInvocationStream, intermediateDataset);
        stopWatch.stop();
        return stopWatch;
    }

    private void invokeOperations(double tolerableErrorPercentage,
                                  Stream<Invokable> invokableStream,
                                  Stream<OperationInvocation> operationInvocationStream,
                                  Stack<IntermediateDataset> intermediateDataset) {
        Streams.forEachPair(invokableStream,
                operationInvocationStream,
                (invokable, operationInvocation) -> invokeOperation(invokable,
                        operationInvocation,
                        intermediateDataset,
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
                                 final Stack<IntermediateDataset> intermediateDataset,
                                 final double tolerableErrorPercentage) {
        InvocationParameters invocationParameters = createInvocationParameters(operationInvocation,
                intermediateDataset,
                tolerableErrorPercentage);
        IntermediateDataset outputDataset = invokable.invoke(invocationParameters);
        for (int i = 0; i < operationInvocation.getSucceedingPhysicalOperationsCount(); ++i) {
            intermediateDataset.push(outputDataset);
        }
    }

    private InvocationParameters createInvocationParameters(final OperationInvocation operationInvocation,
                                                            final Stack<IntermediateDataset> intermediateDatasetStack,
                                                            final double tolerableErrorPercentage) {
        return InvocationParameters.builder()
                .physicalOperation(operationInvocation.getPhysicalOperation())
                .inputIntermediateDatasets(getInputIntermediateDatasets(operationInvocation.getPrecedingPhysicalOperationsCount(), intermediateDatasetStack))
                .tolerableErrorPercentage(tolerableErrorPercentage)
                .build();
    }

    private List<IntermediateDataset> getInputIntermediateDatasets(final int successivePhysicalOperationsCount,
                                                                   final Stack<IntermediateDataset> intermediateDatasetStack) {
        List<IntermediateDataset> intermediateDatasets = Lists.newArrayList();
        for (int i = 0; i < successivePhysicalOperationsCount; ++i) {
            intermediateDatasets.add(intermediateDatasetStack.pop());
        }
        return intermediateDatasets;
    }
}
