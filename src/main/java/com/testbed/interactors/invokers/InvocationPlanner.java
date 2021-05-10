package com.testbed.interactors.invokers;

import com.clearspring.analytics.util.Lists;
import com.google.common.base.Functions;
import com.google.common.graph.Graph;
import com.testbed.entities.invocations.InvocationPlan;
import com.testbed.entities.invocations.OperationInvocation;
import com.testbed.entities.operations.physical.PhysicalOperation;
import com.testbed.entities.operations.physical.PhysicalPlan;
import com.testbed.entities.operations.physical.PhysicalSink;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;

@SuppressWarnings("UnstableApiUsage")
public class InvocationPlanner {
    public InvocationPlan createInvocationPlan(final PhysicalPlan physicalPlan) {
        Stack<PhysicalOperation> physicalOperationStack = new Stack<>();
        List<OperationInvocation> operationInvocations = Lists.newArrayList();
        Map<PhysicalOperation, Integer> inputDependencyCounter = countInputDependencies(physicalPlan);
        movePhysicalOperationsWithoutDependenciesFromMapToStack(physicalOperationStack, inputDependencyCounter);
        while (!physicalOperationStack.empty()) {
            PhysicalOperation currentPhysicalOperation = physicalOperationStack.pop();
            operationInvocations.add(createOperationInvocation(currentPhysicalOperation, physicalPlan.getGraph()));
            decreaseInputDependencyCounterForAllSuccessors(physicalPlan, inputDependencyCounter, currentPhysicalOperation);
            movePhysicalOperationsWithoutDependenciesFromMapToStack(physicalOperationStack, inputDependencyCounter);
        }
        return new InvocationPlan(operationInvocations);
    }

    private Map<PhysicalOperation, Integer> countInputDependencies(final PhysicalPlan physicalPlan) {
        Graph<PhysicalOperation> physicalOperationGraph = physicalPlan.getGraph();
        return physicalOperationGraph.nodes().stream()
                .collect(Collectors.toMap(Functions.identity(), physicalOperationGraph::inDegree));
    }

    private void movePhysicalOperationsWithoutDependenciesFromMapToStack(final Stack<PhysicalOperation> physicalOperationStack,
                                                                         final Map<PhysicalOperation, Integer> inputDependencyCounter) {
        List<PhysicalOperation> physicalOperationsWithoutDependencies = getPhysicalOperationsWithoutDependencies(inputDependencyCounter);
        physicalOperationStack.addAll(physicalOperationsWithoutDependencies);
        physicalOperationsWithoutDependencies.forEach(inputDependencyCounter::remove);
    }

    private List<PhysicalOperation> getPhysicalOperationsWithoutDependencies(final Map<PhysicalOperation, Integer> inputDependencyCounter) {
        return inputDependencyCounter.entrySet().stream()
                .filter(physicalOperationLongEntry -> physicalOperationLongEntry.getValue() == 0)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private OperationInvocation createOperationInvocation(final PhysicalOperation currentPhysicalOperation,
                                                          final Graph<PhysicalOperation> graph) {
        return OperationInvocation.builder()
                .physicalOperation(currentPhysicalOperation)
                .precedingPhysicalOperationsCount(graph.inDegree(currentPhysicalOperation))
                .succeedingPhysicalOperationsCount(graph.outDegree(currentPhysicalOperation))
                .isLastOperationBeforeSink(graph.successors(currentPhysicalOperation).stream()
                        .anyMatch(physicalOperation -> physicalOperation instanceof PhysicalSink))
                .build();
    }

    private void decreaseInputDependencyCounterForAllSuccessors(final PhysicalPlan physicalPlan,
                                                                final Map<PhysicalOperation, Integer> inputDependencyCounter,
                                                                final PhysicalOperation currentPhysicalOperation) {
        Collection<PhysicalOperation> successors = physicalPlan.getGraph().successors(currentPhysicalOperation);
        successors.forEach(successor -> inputDependencyCounter.put(successor, inputDependencyCounter.get(successor) - 1));
    }
}
