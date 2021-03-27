package interactors.converters.logicalToPhysical;

import boundary.deserializers.ProfileDeserializer;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import entities.exceptions.ColumnNotFoundException;
import entities.operations.logical.LogicalLoad;
import entities.operations.logical.LogicalOperation;
import entities.operations.logical.LogicalPlan;
import entities.operations.physical.PhysicalLoad;
import entities.operations.physical.PhysicalOperation;
import entities.operations.physical.PhysicalPlan;
import entities.operations.physical.PhysicalSink;
import entities.profiles.ProfileEstimation;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class LogicalOperationsConverter {
    private final ProfileDeserializer profileDeserializer;
    private final Map<String, LogicalOperationConverter> logicalOperationConverterMapping;

    public PhysicalPlan convert(LogicalPlan logicalPlan) {
        List<LogicalLoad> logicalLoads = logicalPlan.getLogicalLoads();
        List<ProfileEstimation> loadProfileEstimations = getLoadProfileEstimations(logicalLoads);
        Multimap<PhysicalOperation, PhysicalOperation> graph = createPhysicalGraph(loadProfileEstimations, logicalPlan);
        List<PhysicalLoad> physicalLoads = loadProfileEstimations.stream()
                .map(this::getPhysicalOperation)
                .map(physicalOperation -> (PhysicalLoad) physicalOperation)
                .collect(Collectors.toList());
        return PhysicalPlan.builder()
                .graph(graph)
                .loadOperations(physicalLoads)
                .build();
    }

    private List<ProfileEstimation> getLoadProfileEstimations(List<LogicalLoad> logicalLoads) {
        return logicalLoads.stream()
                .map(logicalLoad -> ProfileEstimation.builder()
                        .logicalOperation(logicalLoad)
                        .profile(profileDeserializer.deserialize(logicalLoad.getDatasetDirectoryPath()))
                        .columnStatsPath(logicalLoad.getDatasetDirectoryPath())
                        .build())
                .collect(Collectors.toList());
    }

    private Multimap<PhysicalOperation, PhysicalOperation> createPhysicalGraph(List<ProfileEstimation> loadProfileEstimations,
                                                                               LogicalPlan logicalPlan) {
        Multimap<LogicalOperation, LogicalOperation> logicalGraph = logicalPlan.getGraph();
        Multimap<PhysicalOperation, PhysicalOperation> physicalGraph = MultimapBuilder.hashKeys().arrayListValues().build();

        Stack<ProfileEstimation> operationsStack = new Stack<>();
        operationsStack.addAll(loadProfileEstimations);
        while (!operationsStack.isEmpty()) {
            ProfileEstimation currentProfileEstimation = operationsStack.pop();
            Collection<LogicalOperation> adjacentLogicalOperations = logicalGraph.get(currentProfileEstimation.getLogicalOperation());
            List<ProfileEstimation> adjacentProfileEstimations = getAdjacentProfileEstimations(currentProfileEstimation,
                    adjacentLogicalOperations);
            operationsStack.addAll(adjacentProfileEstimations);
            PhysicalOperation currentPhysicalOperation = getPhysicalOperation(currentProfileEstimation);
            adjacentProfileEstimations.stream()
                    .map(this::getPhysicalOperation)
                    .forEach(adjacentPhysicalOperation -> physicalGraph.put(currentPhysicalOperation, adjacentPhysicalOperation));
            if (adjacentProfileEstimations.isEmpty()) {
                physicalGraph.put(currentPhysicalOperation, new PhysicalSink());
            }
        }
        return physicalGraph;
    }

    private PhysicalOperation getPhysicalOperation(ProfileEstimation profileEstimation) throws ColumnNotFoundException {
        String logicalOperationName = profileEstimation.getLogicalOperation().getClass().getSimpleName();
        return logicalOperationConverterMapping.get(logicalOperationName).convert(profileEstimation);
    }

    private List<ProfileEstimation> getAdjacentProfileEstimations(ProfileEstimation currentProfileEstimation,
                                                                  Collection<LogicalOperation> adjacentLogicalOperations) {
        return adjacentLogicalOperations.stream()
                .map(logicalOperation -> ProfileEstimation.builder()
                        .logicalOperation(logicalOperation)
                        .profile(currentProfileEstimation.getProfile())
                        .columnStatsPath(currentProfileEstimation.getColumnStatsPath())
                        .build())
                .collect(Collectors.toList());
    }
}
