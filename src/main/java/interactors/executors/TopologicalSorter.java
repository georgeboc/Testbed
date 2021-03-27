package interactors.executors;

import com.clearspring.analytics.util.Lists;
import com.google.common.base.Functions;
import entities.operations.physical.PhysicalOperation;
import entities.operations.physical.PhysicalPlan;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;

public class TopologicalSorter {
    public List<PhysicalOperation> sortTopologically(PhysicalPlan physicalPlan) {
        Stack<PhysicalOperation> physicalOperationStack = new Stack<>();
        List<PhysicalOperation> topologicallySortedPhysicalOperations = Lists.newArrayList();
        Map<PhysicalOperation, Long> inputDependencyCounter = countInputDependencies(physicalPlan);
        physicalOperationStack.addAll(getPhysicalOperationsWithoutDependencies(inputDependencyCounter));
        while (!physicalOperationStack.empty()) {
            PhysicalOperation currentPhysicalOperation = physicalOperationStack.pop();
            inputDependencyCounter.remove(currentPhysicalOperation);
            topologicallySortedPhysicalOperations.add(currentPhysicalOperation);
            Collection<PhysicalOperation> dependants = physicalPlan.getGraph().get(currentPhysicalOperation);
            dependants.forEach(dependant -> inputDependencyCounter.put(dependant, inputDependencyCounter.get(dependant) - 1));
            physicalOperationStack.addAll(getPhysicalOperationsWithoutDependencies(inputDependencyCounter));
        }
        return topologicallySortedPhysicalOperations;
    }

    private List<PhysicalOperation> getPhysicalOperationsWithoutDependencies(Map<PhysicalOperation, Long> inputDependencyCounter) {
        return inputDependencyCounter.entrySet().stream()
                .filter(physicalOperationLongEntry -> physicalOperationLongEntry.getValue() == 0)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private Map<PhysicalOperation, Long> countInputDependencies(PhysicalPlan physicalPlan) {
        Map<PhysicalOperation, Long> inputDependenciesCounter = physicalPlan.getGraph().values().stream()
                .collect(Collectors.groupingBy(Functions.identity(), Collectors.counting()));
        Map<PhysicalOperation, Long> noInputDependencies = physicalPlan.getLoadOperations().stream()
                .map(physicalLoad -> (PhysicalOperation) physicalLoad)
                .collect(Collectors.toMap(Functions.identity(), Functions.constant(0L)));
        inputDependenciesCounter.putAll(noInputDependencies);
        return inputDependenciesCounter;
    }
}
