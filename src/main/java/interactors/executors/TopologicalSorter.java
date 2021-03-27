package interactors.executors;

import com.clearspring.analytics.util.Lists;
import com.google.common.base.Functions;
import com.google.common.graph.Graph;
import entities.operations.physical.PhysicalOperation;
import entities.operations.physical.PhysicalPlan;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;

@SuppressWarnings("UnstableApiUsage")
public class TopologicalSorter {
    public List<PhysicalOperation> sortTopologically(PhysicalPlan physicalPlan) {
        Stack<PhysicalOperation> physicalOperationStack = new Stack<>();
        List<PhysicalOperation> topologicallySortedPhysicalOperations = Lists.newArrayList();
        Map<PhysicalOperation, Integer> inputDependencyCounter = countInputDependencies(physicalPlan);
        physicalOperationStack.addAll(getPhysicalOperationsWithoutDependencies(inputDependencyCounter));
        while (!physicalOperationStack.empty()) {
            PhysicalOperation currentPhysicalOperation = physicalOperationStack.pop();
            inputDependencyCounter.remove(currentPhysicalOperation);
            topologicallySortedPhysicalOperations.add(currentPhysicalOperation);
            decreaseInputDependencyCounterForAllSuccessors(physicalPlan, inputDependencyCounter, currentPhysicalOperation);
            physicalOperationStack.addAll(getPhysicalOperationsWithoutDependencies(inputDependencyCounter));
        }
        return topologicallySortedPhysicalOperations;
    }

    private void decreaseInputDependencyCounterForAllSuccessors(PhysicalPlan physicalPlan,
                                                                Map<PhysicalOperation, Integer> inputDependencyCounter,
                                                                PhysicalOperation currentPhysicalOperation) {
        Collection<PhysicalOperation> successors = physicalPlan.getGraph().successors(currentPhysicalOperation);
        successors.forEach(successor -> inputDependencyCounter.put(successor, inputDependencyCounter.get(successor) - 1));
    }

    private List<PhysicalOperation> getPhysicalOperationsWithoutDependencies(Map<PhysicalOperation, Integer> inputDependencyCounter) {
        return inputDependencyCounter.entrySet().stream()
                .filter(physicalOperationLongEntry -> physicalOperationLongEntry.getValue() == 0)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private Map<PhysicalOperation, Integer> countInputDependencies(PhysicalPlan physicalPlan) {
        Graph<PhysicalOperation> physicalOperationGraph = physicalPlan.getGraph();
        return physicalOperationGraph.nodes().stream()
                .collect(Collectors.toMap(Functions.identity(), physicalOperationGraph::inDegree));
    }
}
