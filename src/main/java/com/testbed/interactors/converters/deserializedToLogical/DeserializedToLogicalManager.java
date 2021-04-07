package com.testbed.interactors.converters.deserializedToLogical;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Streams;
import com.google.common.graph.Graph;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import com.testbed.entities.operations.deserialized.DeserializedLoad;
import com.testbed.entities.operations.deserialized.DeserializedOperation;
import com.testbed.entities.operations.deserialized.DeserializedOperations;
import com.testbed.entities.operations.logical.LogicalLoad;
import com.testbed.entities.operations.logical.LogicalOperation;
import com.testbed.entities.operations.logical.LogicalPlan;
import com.testbed.interactors.converters.deserializedToLogical.inputTagStream.InputTagsStream;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils;
import org.springframework.context.ApplicationContext;

import javax.inject.Inject;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.testbed.configuration.SpringConfiguration.DESERIALIZED_LOAD;

@SuppressWarnings("UnstableApiUsage")
@RequiredArgsConstructor
public class DeserializedToLogicalManager {
    @Inject
    private ApplicationContext applicationContext;

    public LogicalPlan convert(final DeserializedOperations deserializedOperations) {
        Map<DeserializedOperation, LogicalOperation> operationsMapping = getOperationsMapping(deserializedOperations);
        List<DeserializedLoad> deserializedLoads = getDeserializedLoads(deserializedOperations);
        Multimap<String, DeserializedOperation> mappingByInputTag = getMappingByInputTag(deserializedOperations);
        Graph<LogicalOperation> graph = createLogicalGraph(deserializedLoads, mappingByInputTag, operationsMapping);
        List<LogicalLoad> logicalLoads = getLogicalLoads(operationsMapping, deserializedLoads);
        return LogicalPlan.builder()
                .graph(graph)
                .logicalLoads(logicalLoads)
                .build();
    }

    private Map<DeserializedOperation, LogicalOperation> getOperationsMapping(final DeserializedOperations deserializedOperations) {
        Stream<LogicalOperation> logicalOperationsStream = deserializedOperations.stream()
                .map(this::convertDeserializedToLogicalOperation);
        return Streams.zip(deserializedOperations.stream(), logicalOperationsStream, AbstractMap.SimpleEntry::new)
                .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
    }

    private LogicalOperation convertDeserializedToLogicalOperation(DeserializedOperation deserializedOperation) {
        return BeanFactoryAnnotationUtils.qualifiedBeanOfType(applicationContext.getAutowireCapableBeanFactory(),
                DeserializedToLogicalConverter.class,
                deserializedOperation.getClass().getSimpleName()).convert(deserializedOperation);
    }

    private List<DeserializedLoad> getDeserializedLoads(final DeserializedOperations deserializedOperations) {
        return deserializedOperations.stream()
                .filter(deserializedOperation -> deserializedOperation.getClass().getSimpleName().equals(DESERIALIZED_LOAD))
                .map(deserializedOperation -> (DeserializedLoad) deserializedOperation)
                .collect(Collectors.toList());
    }

    private Graph<LogicalOperation> createLogicalGraph(final List<DeserializedLoad> deserializedLoads,
                                                       final Multimap<String, DeserializedOperation> mappingByInputTag,
                                                       final Map<DeserializedOperation, LogicalOperation> operationsMapping) {
        MutableGraph<LogicalOperation> logicalGraph = GraphBuilder.directed().build();
        Queue<DeserializedOperation> queue = Lists.newLinkedList();
        queue.addAll(deserializedLoads);
        while (!queue.isEmpty()) {
            DeserializedOperation currentDeserializedOperation = queue.remove();
            String outputTag = currentDeserializedOperation.getOutputTag();
            Collection<DeserializedOperation> succeedingDeserializedOperations = mappingByInputTag.get(outputTag);
            List<DeserializedOperation> unvisitedDeserializedOperations = getUnvisitedDeserializedOperations(logicalGraph,
                    succeedingDeserializedOperations,
                    operationsMapping);
            queue.addAll(unvisitedDeserializedOperations);
            addLogicalGraphEdges(operationsMapping,
                    logicalGraph,
                    currentDeserializedOperation,
                    succeedingDeserializedOperations);
        }
        return logicalGraph;
    }

    private void addLogicalGraphEdges(Map<DeserializedOperation, LogicalOperation> operationsMapping,
                                      MutableGraph<LogicalOperation> logicalGraph,
                                      DeserializedOperation currentDeserializedOperation,
                                      Collection<DeserializedOperation> successiveDeserializedOperations) {
        LogicalOperation currentLogicalOperation = operationsMapping.get(currentDeserializedOperation);
        successiveDeserializedOperations.forEach(successiveDeserializedOperation ->
                logicalGraph.putEdge(currentLogicalOperation, operationsMapping.get(successiveDeserializedOperation)));
    }

    private List<DeserializedOperation> getUnvisitedDeserializedOperations(final MutableGraph<LogicalOperation> logicalGraph,
                                                                           final Collection<DeserializedOperation> deserializedOperations,
                                                                           final Map<DeserializedOperation, LogicalOperation> operationsMapping) {
        return deserializedOperations.stream()
                .filter(deserializedOperation -> !logicalGraph.nodes().contains(operationsMapping.get(deserializedOperation)))
                .collect(Collectors.toList());
    }

    private Multimap<String, DeserializedOperation> getMappingByInputTag(final List<DeserializedOperation> deserializedOperations) {
        Stream<Stream<String>> inputTagStreamOfStreams = deserializedOperations.stream()
                .map(this::getInputTagStream);
        return Streams.zip(inputTagStreamOfStreams, deserializedOperations.stream(), InputTagsStreamAndOperation::new)
                .flatMap(inputTagsStreamAndOperation -> inputTagsStreamAndOperation.getInputTagsStream()
                        .map(inputTag -> new AbstractMap.SimpleEntry<>(inputTag,
                                inputTagsStreamAndOperation.getDeserializedOperation())))
                .collect(ArrayListMultimap::create,
                        (multimap, simpleEntry) -> multimap.put(simpleEntry.getKey(), simpleEntry.getValue()),
                        Multimap::putAll);
    }

    private Stream<String> getInputTagStream(DeserializedOperation deserializedOperation) {
        return BeanFactoryAnnotationUtils.qualifiedBeanOfType(applicationContext.getAutowireCapableBeanFactory(),
                InputTagsStream.class, deserializedOperation.getClass().getSimpleName()).getInputTagStream(deserializedOperation);
    }

    private List<LogicalLoad> getLogicalLoads(final Map<DeserializedOperation, LogicalOperation> operationsMapping,
                                              final List<DeserializedLoad> deserializedLoads) {
        return deserializedLoads.stream()
                .map(operationsMapping::get)
                .map(logicalOperation -> (LogicalLoad) logicalOperation)
                .collect(Collectors.toList());
    }

    @Data
    private static class InputTagsStreamAndOperation {
        private final Stream<String> inputTagsStream;
        private final DeserializedOperation deserializedOperation;
    }
}
