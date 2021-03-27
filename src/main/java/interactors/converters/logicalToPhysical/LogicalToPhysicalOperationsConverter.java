package interactors.converters.logicalToPhysical;

import boundary.deserializers.ProfileDeserializer;
import com.google.common.graph.Graph;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
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
@SuppressWarnings("UnstableApiUsage")
public class LogicalToPhysicalOperationsConverter {
    private final ProfileDeserializer profileDeserializer;
    private final Map<String, LogicalToPhysicalOperationConverter> logicalOperationConverterMapping;

    public PhysicalPlan convert(LogicalPlan logicalPlan) {
        List<LogicalLoad> logicalLoads = logicalPlan.getLogicalLoads();
        List<ProfileEstimation> loadProfileEstimations = getLoadProfileEstimations(logicalLoads);
        Graph<PhysicalOperation> graph = createPhysicalGraph(loadProfileEstimations, logicalPlan);
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

    private Graph<PhysicalOperation> createPhysicalGraph(List<ProfileEstimation> loadProfileEstimations,
                                                         LogicalPlan logicalPlan) {
        MutableGraph<PhysicalOperation> physicalGraph = GraphBuilder.directed().build();

        Stack<ProfileEstimation> operationsStack = new Stack<>();
        operationsStack.addAll(loadProfileEstimations);
        while (!operationsStack.isEmpty()) {
            ProfileEstimation currentProfileEstimation = operationsStack.pop();
            Collection<LogicalOperation> successiveLogicalOperations = logicalPlan.getGraph().successors(currentProfileEstimation.getLogicalOperation());
            List<ProfileEstimation> successiveProfileEstimations = getSuccessiveProfileEstimations(currentProfileEstimation, successiveLogicalOperations);
            operationsStack.addAll(successiveProfileEstimations);
            PhysicalOperation currentPhysicalOperation = getPhysicalOperation(currentProfileEstimation);
            successiveProfileEstimations.stream()
                    .map(this::getPhysicalOperation)
                    .forEach(successivePhysicalOperation -> physicalGraph.putEdge(currentPhysicalOperation, successivePhysicalOperation));
            if (successiveProfileEstimations.isEmpty()) {
                physicalGraph.putEdge(currentPhysicalOperation, new PhysicalSink());
            }
        }
        return physicalGraph;
    }

    private PhysicalOperation getPhysicalOperation(ProfileEstimation profileEstimation) throws ColumnNotFoundException {
        String logicalOperationName = profileEstimation.getLogicalOperation().getClass().getSimpleName();
        return logicalOperationConverterMapping.get(logicalOperationName).convert(profileEstimation);
    }

    private List<ProfileEstimation> getSuccessiveProfileEstimations(ProfileEstimation currentProfileEstimation,
                                                                    Collection<LogicalOperation> successiveLogicalOperations) {
        return successiveLogicalOperations.stream()
                .map(logicalOperation -> ProfileEstimation.builder()
                        .logicalOperation(logicalOperation)
                        .profile(currentProfileEstimation.getProfile())
                        .columnStatsPath(currentProfileEstimation.getColumnStatsPath())
                        .build())
                .collect(Collectors.toList());
    }
}
