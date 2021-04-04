package com.testbed.interactors.converters.logicalToPhysical;

import com.google.common.graph.Graph;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import com.testbed.boundary.deserializers.Deserializer;
import com.testbed.entities.exceptions.ColumnNotFoundException;
import com.testbed.entities.operations.logical.LogicalLoad;
import com.testbed.entities.operations.logical.LogicalOperation;
import com.testbed.entities.operations.logical.LogicalPlan;
import com.testbed.entities.operations.physical.PhysicalLoad;
import com.testbed.entities.operations.physical.PhysicalOperation;
import com.testbed.entities.operations.physical.PhysicalPlan;
import com.testbed.entities.operations.physical.PhysicalSink;
import com.testbed.entities.profiles.Profile;
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
public class LogicalToPhysicalManager {
    private final Deserializer<Profile> profileDeserializer;
    private final Map<String, LogicalToPhysicalConverter> logicalOperationConverterMapping;

    public PhysicalPlan convert(final LogicalPlan logicalPlan) {
        List<LogicalLoad> logicalLoads = logicalPlan.getLogicalLoads();
        List<ProfileEstimation> loadProfileEstimations = getLoadProfileEstimations(logicalLoads);
        Graph<PhysicalOperation> graph = createPhysicalGraph(loadProfileEstimations, logicalPlan);
        List<PhysicalLoad> physicalLoads = getPhysicalLoads(loadProfileEstimations);
        return PhysicalPlan.builder()
                .graph(graph)
                .loadOperations(physicalLoads)
                .build();
    }

    private List<ProfileEstimation> getLoadProfileEstimations(final List<LogicalLoad> logicalLoads) {
        return logicalLoads.stream()
                .map(logicalLoad -> ProfileEstimation.builder()
                        .logicalOperation(logicalLoad)
                        .profile(profileDeserializer.deserialize(logicalLoad.getDatasetDirectoryPath()))
                        .columnStatsPath(logicalLoad.getDatasetDirectoryPath())
                        .build())
                .collect(Collectors.toList());
    }

    private Graph<PhysicalOperation> createPhysicalGraph(final List<ProfileEstimation> loadProfileEstimations,
                                                         final LogicalPlan logicalPlan) {
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
                physicalGraph.putEdge(currentPhysicalOperation, new PhysicalSink(String.valueOf(currentPhysicalOperation.hashCode())));
            }
            visitedProfileEstimations.add(currentProfileEstimation);
        }
        return physicalGraph;
    }

    private Collection<ProfileEstimation> getUnvisitedSuccessiveProfileEstimations(final List<ProfileEstimation> successiveProfileEstimations,
                                                                                   final Set<ProfileEstimation> visitedProfileEstimations) {
        return successiveProfileEstimations.stream()
                .filter(successiveProfileEstimation -> !visitedProfileEstimations.contains(successiveProfileEstimation))
                .collect(Collectors.toList());
    }

    private PhysicalOperation getPhysicalOperation(final ProfileEstimation profileEstimation) throws ColumnNotFoundException {
        String logicalOperationName = profileEstimation.getLogicalOperation().getClass().getSimpleName();
        return logicalOperationConverterMapping.get(logicalOperationName).convert(profileEstimation);
    }

    private List<ProfileEstimation> getSuccessiveProfileEstimations(final ProfileEstimation currentProfileEstimation,
                                                                    final Collection<LogicalOperation> successiveLogicalOperations) {
        return successiveLogicalOperations.stream()
                .map(logicalOperation -> ProfileEstimation.builder()
                        .logicalOperation(logicalOperation)
                        .profile(currentProfileEstimation.getProfile())
                        .columnStatsPath(currentProfileEstimation.getColumnStatsPath())
                        .build())
                .collect(Collectors.toList());
    }

    private List<PhysicalLoad> getPhysicalLoads(final List<ProfileEstimation> loadProfileEstimations) {
        return loadProfileEstimations.stream()
                .map(loadProfileEstimation -> logicalOperationConverterMapping
                        .get(loadProfileEstimation.getLogicalOperation().getClass().getSimpleName()).convert(loadProfileEstimation))
                .map(physicalOperation -> (PhysicalLoad) physicalOperation)
                .collect(Collectors.toList());
    }
}
