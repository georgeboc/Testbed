package interactors.converters.deserializedToLogical;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.graph.Graph;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import entities.operations.deserialized.DeserializedOperation;
import entities.operations.deserialized.DeserializedOperations;
import entities.operations.logical.LogicalLoad;
import entities.operations.logical.LogicalOperation;
import entities.operations.logical.LogicalPlan;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@SuppressWarnings("UnstableApiUsage")
public class DeserializedToLogicalOperationsConverter {
    private final static Logger LOG = Logger.getLogger(DeserializedToLogicalOperationsConverter.class.getName());
    private static final String LOGICAL_LOAD = "LogicalLoad";
    private final Map<String, DeserializedToLogicalOperationConverter> deserializedOperationConverterMapping;

    public LogicalPlan convert(DeserializedOperations deserializedOperations) {
        List<Mapping> mappings = getMappings(deserializedOperations);
        List<Mapping> loadMappings = getLoadMappings(mappings);
        Graph<LogicalOperation> graph = createLogicalGraph(loadMappings, mappings);
        List<LogicalLoad> logicalLoads = getLogicalLoads(loadMappings);
        return LogicalPlan.builder()
                .graph(graph)
                .logicalLoads(logicalLoads)
                .build();
    }

    private List<Mapping> getMappings(DeserializedOperations deserializedOperations) {
        List<Mapping> mappings = Lists.newLinkedList();
        for (DeserializedOperation deserializedOperation: deserializedOperations) {
            DeserializedToLogicalOperationConverter deserializedToLogicalOperationConverter = deserializedOperationConverterMapping
                    .get(deserializedOperation.getClass().getSimpleName());
            LogicalOperation logicalOperation = deserializedToLogicalOperationConverter.convert(deserializedOperation);
            mappings.add(new Mapping(logicalOperation, deserializedOperation));
        }
        return mappings;
    }

    private List<Mapping> getLoadMappings(List<Mapping> mappings) {
        return mappings.stream()
                .filter(mapping -> mapping.getLogicalOperation().getClass().getSimpleName().equals(LOGICAL_LOAD))
                .collect(Collectors.toList());
    }

    private Graph<LogicalOperation> createLogicalGraph(List<Mapping> loadMappings, List<Mapping> mappings) {
        Multimap<String, Mapping> mappingByTags = getMappingByTag(mappings);
        MutableGraph<LogicalOperation> logicalGraph = GraphBuilder.directed().build();
        Queue<Mapping> mappingQueue = Lists.newLinkedList();
        mappingQueue.addAll(loadMappings);
        while (!mappingQueue.isEmpty()) {
            Mapping currentMapping = mappingQueue.remove();
            String outputTag = currentMapping.getDeserializedOperation().getOutputTag();
            Collection<Mapping> successiveMappings = mappingByTags.get(outputTag);
            mappingQueue.addAll(getUnvisitedMappings(logicalGraph, successiveMappings));
            successiveMappings.forEach(successiveMapping ->
                    logicalGraph.putEdge(currentMapping.getLogicalOperation(), successiveMapping.getLogicalOperation()));
        }
        return logicalGraph;
    }

    private List<Mapping> getUnvisitedMappings(MutableGraph<LogicalOperation> logicalGraph, Collection<Mapping> successiveMappings) {
        return successiveMappings.stream().filter(mapping -> !logicalGraph.nodes().contains(mapping.getLogicalOperation())).collect(Collectors.toList());
    }

    private Multimap<String, Mapping> getMappingByTag(List<Mapping> mappings) {
        return mappings.stream()
                .filter(mapping -> (mapping.getDeserializedOperation().getInputTag() != null))
                .collect(ArrayListMultimap::create,
                        (multimap, mapping) -> multimap.put(mapping.getDeserializedOperation().getInputTag(), mapping),
                        Multimap::putAll);
    }

    private List<LogicalLoad> getLogicalLoads(List<Mapping> loadMappings) {
        return loadMappings.stream()
                .map(loadMapping -> (LogicalLoad) loadMapping.getLogicalOperation())
                .collect(Collectors.toList());
    }

    @Data
    private static class Mapping {
        private final LogicalOperation logicalOperation;
        private final DeserializedOperation deserializedOperation;
    }
}
