package com.testbed.interactors.jobs;

import com.clearspring.analytics.util.Lists;
import com.google.common.base.Functions;
import com.google.common.graph.Graph;
import com.testbed.entities.jobs.Job;
import com.testbed.entities.jobs.JobOperation;
import com.testbed.entities.operations.physical.PhysicalOperation;
import com.testbed.entities.operations.physical.PhysicalPlan;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;

@SuppressWarnings("UnstableApiUsage")
public class JobCreator {
    public Job createJob(final PhysicalPlan physicalPlan) {
        Stack<PhysicalOperation> physicalOperationStack = new Stack<>();
        List<JobOperation> jobOperations = Lists.newArrayList();
        Map<PhysicalOperation, Integer> inputDependencyCounter = countInputDependencies(physicalPlan);
        physicalOperationStack.addAll(getPhysicalOperationsWithoutDependencies(inputDependencyCounter));
        while (!physicalOperationStack.empty()) {
            PhysicalOperation currentPhysicalOperation = physicalOperationStack.pop();
            inputDependencyCounter.remove(currentPhysicalOperation);
            jobOperations.add(createJobOperation(currentPhysicalOperation, physicalPlan.getGraph()));
            decreaseInputDependencyCounterForAllSuccessors(physicalPlan, inputDependencyCounter, currentPhysicalOperation);
            physicalOperationStack.addAll(getPhysicalOperationsWithoutDependencies(inputDependencyCounter));
        }
        return new Job(jobOperations);
    }

    private JobOperation createJobOperation(final PhysicalOperation currentPhysicalOperation,
                                            final Graph<PhysicalOperation> graph) {
        return new JobOperation(currentPhysicalOperation,
                graph.inDegree(currentPhysicalOperation),
                graph.outDegree(currentPhysicalOperation));
    }

    private void decreaseInputDependencyCounterForAllSuccessors(final PhysicalPlan physicalPlan,
                                                                final Map<PhysicalOperation, Integer> inputDependencyCounter,
                                                                final PhysicalOperation currentPhysicalOperation) {
        Collection<PhysicalOperation> successors = physicalPlan.getGraph().successors(currentPhysicalOperation);
        successors.forEach(successor -> inputDependencyCounter.put(successor, inputDependencyCounter.get(successor) - 1));
    }

    private List<PhysicalOperation> getPhysicalOperationsWithoutDependencies(final Map<PhysicalOperation, Integer> inputDependencyCounter) {
        return inputDependencyCounter.entrySet().stream()
                .filter(physicalOperationLongEntry -> physicalOperationLongEntry.getValue() == 0)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private Map<PhysicalOperation, Integer> countInputDependencies(final PhysicalPlan physicalPlan) {
        Graph<PhysicalOperation> physicalOperationGraph = physicalPlan.getGraph();
        return physicalOperationGraph.nodes().stream()
                .collect(Collectors.toMap(Functions.identity(), physicalOperationGraph::inDegree));
    }
}
