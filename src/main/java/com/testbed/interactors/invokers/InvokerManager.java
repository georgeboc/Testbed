package com.testbed.interactors.invokers;

import com.clearspring.analytics.util.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Streams;
import com.testbed.boundary.invocations.InvocationParameters;
import com.testbed.boundary.invocations.Invokable;
import com.testbed.boundary.invocations.intermediateDatasets.IntermediateDataset;
import com.testbed.entities.invocations.InvocationPlan;
import com.testbed.entities.invocations.OperationInvocation;
import com.testbed.entities.operations.physical.PhysicalOperation;
import com.testbed.interactors.monitors.Monitor;
import com.testbed.interactors.monitors.MonitoringInformation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils;
import org.springframework.context.ApplicationContext;

import javax.inject.Inject;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

@SuppressWarnings("UnstableApiUsage")
@RequiredArgsConstructor
public class InvokerManager {
    private final Monitor monitor;

    @Inject
    private ApplicationContext applicationContext;

    public MonitoringInformation invoke(final InvocationPlan invocationPlan, final double tolerableErrorPercentage) {
        Stream<Invokable> invokableStream = getInvokableStream(invocationPlan.getOperationInvocations());
        Stream<OperationInvocation> operationInvocationStream = invocationPlan.getOperationInvocations().stream();
        Multimap<PhysicalOperation, IntermediateDataset> intermediateDatasets = MultimapBuilder.hashKeys()
                .arrayListValues()
                .build();
        return monitor.monitor(getInvokeOperationsCallable(tolerableErrorPercentage,
                invokableStream,
                operationInvocationStream,
                intermediateDatasets), invocationPlan);
    }

    private Callable<MonitoringInformation> getInvokeOperationsCallable(final double tolerableErrorPercentage,
                                                                        final Stream<Invokable> invokableStream,
                                                                        final Stream<OperationInvocation> operationInvocationStream,
                                                                        final Multimap<PhysicalOperation, IntermediateDataset> intermediateDatasets) {
        return Executors.callable(() -> invokeOperations(tolerableErrorPercentage,
                invokableStream,
                operationInvocationStream,
                intermediateDatasets), MonitoringInformation.createNew());
    }

    private void invokeOperations(final double tolerableErrorPercentage,
                                  final Stream<Invokable> invokableStream,
                                  final Stream<OperationInvocation> operationInvocationStream,
                                  final Multimap<PhysicalOperation, IntermediateDataset> intermediateDatasets) {
        Streams.forEachPair(invokableStream,
                operationInvocationStream,
                (invokable, operationInvocation) -> invokeOperation(invokable,
                        operationInvocation,
                        intermediateDatasets,
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
                                 final Multimap<PhysicalOperation, IntermediateDataset> intermediateDatasets,
                                 final double tolerableErrorPercentage) {
        InvocationParameters invocationParameters = createInvocationParameters(operationInvocation,
                intermediateDatasets,
                tolerableErrorPercentage);
        IntermediateDataset outputDataset = invokable.invoke(invocationParameters);
        operationInvocation.getSucceedingPhysicalOperations()
                .forEach(succeedingOperationInvocation -> intermediateDatasets.put(succeedingOperationInvocation,
                        outputDataset));
        intermediateDatasets.removeAll(operationInvocation.getPhysicalOperation());
    }

    private InvocationParameters createInvocationParameters(final OperationInvocation operationInvocation,
                                                            final Multimap<PhysicalOperation, IntermediateDataset> intermediateDatasets,
                                                            final double tolerableErrorPercentage) {
        return InvocationParameters.builder()
                .physicalOperation(operationInvocation.getPhysicalOperation())
                .inputIntermediateDatasets(Lists.newArrayList(intermediateDatasets.get(operationInvocation.getPhysicalOperation())))
                .tolerableErrorPercentage(tolerableErrorPercentage)
                .build();
    }
}
