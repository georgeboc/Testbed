package com.testbed.interactors.converters.logicalToPhysical;

import com.testbed.boundary.deserializers.ProfileDeserializer;
import com.google.common.graph.Graph;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import com.testbed.entities.exceptions.ColumnNotFoundException;
import com.testbed.entities.operations.logical.LogicalLoad;
import com.testbed.entities.operations.logical.LogicalOperation;
import com.testbed.entities.operations.logical.LogicalPlan;
import com.testbed.entities.operations.physical.PhysicalLoad;
import com.testbed.entities.operations.physical.PhysicalOperation;
import com.testbed.entities.operations.physical.PhysicalPlan;
import com.testbed.entities.operations.physical.PhysicalSink;
import com.testbed.entities.profiles.ProfileEstimation;
import lombok.RequiredArgsConstructor;
import org.glassfish.jersey.internal.guava.Sets;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
        Set<ProfileEstimation> visitedProfileEstimations = Sets.newHashSet();
        while (!operationsStack.isEmpty()) {
            ProfileEstimation currentProfileEstimation = operationsStack.pop();
            Collection<LogicalOperation> successiveLogicalOperations = logicalPlan.getGraph().successors(currentProfileEstimation.getLogicalOperation());
            List<ProfileEstimation> successiveProfileEstimations = getSuccessiveProfileEstimations(currentProfileEstimation, successiveLogicalOperations);
            operationsStack.addAll(getUnvisitedSuccessiveProfileEstimations(successiveProfileEstimations, visitedProfileEstimations));
            PhysicalOperation currentPhysicalOperation = getPhysicalOperation(currentProfileEstimation);
            successiveProfileEstimations.stream()
                    .map(this::getPhysicalOperation)
                    .forEach(successivePhysicalOperation -> physicalGraph.putEdge(currentPhysicalOperation, successivePhysicalOperation));
            if (successiveProfileEstimations.isEmpty()) {
                physicalGraph.putEdge(currentPhysicalOperation, new PhysicalSink());
            }
            visitedProfileEstimations.add(currentProfileEstimation);
        }
        return physicalGraph;
    }

    private Collection<ProfileEstimation> getUnvisitedSuccessiveProfileEstimations(List<ProfileEstimation> successiveProfileEstimations,
                                                                                   Set<ProfileEstimation> visitedProfileEstimations) {
        return successiveProfileEstimations.stream()
                .filter(successiveProfileEstimation -> !visitedProfileEstimations.contains(successiveProfileEstimation))
                .collect(Collectors.toList());
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
