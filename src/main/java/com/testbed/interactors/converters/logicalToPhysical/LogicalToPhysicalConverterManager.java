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
import org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils;
import org.springframework.context.ApplicationContext;

import javax.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

@SuppressWarnings("UnstableApiUsage")
@RequiredArgsConstructor
public class LogicalToPhysicalConverterManager {
    private static final String SINK_PREFIX_ID = "sink_";
    private final Deserializer<Profile> profileDeserializer;
    @Inject
    private ApplicationContext applicationContext;


    public PhysicalPlan convert(final LogicalPlan logicalPlan, final double tolerableError) {
        List<LogicalLoad> logicalLoads = logicalPlan.getLogicalLoads();
        List<ProfileEstimation> loadProfileEstimations = getLoadProfileEstimations(logicalLoads, tolerableError);
        Graph<PhysicalOperation> graph = createPhysicalGraph(loadProfileEstimations, logicalPlan);
        List<PhysicalLoad> physicalLoads = getPhysicalLoads(loadProfileEstimations);
        return PhysicalPlan.builder()
                .graph(graph)
                .loadOperations(physicalLoads)
                .build();
    }

    private List<ProfileEstimation> getLoadProfileEstimations(final List<LogicalLoad> logicalLoads, final double tolerableError) {
        return logicalLoads.stream()
                .map(logicalLoad -> ProfileEstimation.builder()
                        .logicalOperation(logicalLoad)
                        .profile(profileDeserializer.deserialize(logicalLoad.getDatasetDirectoryPath()))
                        .columnStatsPath(logicalLoad.getDatasetDirectoryPath())
                        .tolerableErrorPercentage(tolerableError)
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
            PhysicalOperation currentPhysicalOperation = convertFromProfileEstimationToPhysicalOperation(currentProfileEstimation);
            successiveProfileEstimations.stream()
                    .map(this::convertFromProfileEstimationToPhysicalOperation)
                    .forEach(successivePhysicalOperation -> physicalGraph.putEdge(currentPhysicalOperation, successivePhysicalOperation));
            if (successiveProfileEstimations.isEmpty()) {
                physicalGraph.putEdge(currentPhysicalOperation, new PhysicalSink(SINK_PREFIX_ID + currentPhysicalOperation.getId()));
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

    private PhysicalOperation convertFromProfileEstimationToPhysicalOperation(final ProfileEstimation profileEstimation) throws ColumnNotFoundException {
        String logicalOperationName = profileEstimation.getLogicalOperation().getClass().getSimpleName();
        return BeanFactoryAnnotationUtils.qualifiedBeanOfType(applicationContext.getAutowireCapableBeanFactory(),
                LogicalToPhysicalConverter.class, logicalOperationName).convert(profileEstimation);
    }

    private List<ProfileEstimation> getSuccessiveProfileEstimations(final ProfileEstimation currentProfileEstimation,
                                                                    final Collection<LogicalOperation> successiveLogicalOperations) {
        return successiveLogicalOperations.stream()
                .map(logicalOperation -> ProfileEstimation.builder()
                        .logicalOperation(logicalOperation)
                        .profile(currentProfileEstimation.getProfile())
                        .columnStatsPath(currentProfileEstimation.getColumnStatsPath())
                        .tolerableErrorPercentage(currentProfileEstimation.getTolerableErrorPercentage())
                        .build())
                .collect(Collectors.toList());
    }

    private List<PhysicalLoad> getPhysicalLoads(final List<ProfileEstimation> loadProfileEstimations) {
        return loadProfileEstimations.stream()
                .map(this::convertFromProfileEstimationToPhysicalLoad)
                .collect(Collectors.toList());
    }

    private PhysicalLoad convertFromProfileEstimationToPhysicalLoad(final ProfileEstimation loadProfileEstimation) {
        return (PhysicalLoad) BeanFactoryAnnotationUtils.qualifiedBeanOfType(applicationContext.getAutowireCapableBeanFactory(),
                LogicalToPhysicalConverter.class, loadProfileEstimation.getLogicalOperation().getClass().getSimpleName())
                .convert(loadProfileEstimation);
    }
}
