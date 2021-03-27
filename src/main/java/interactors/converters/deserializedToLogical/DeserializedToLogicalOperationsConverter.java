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
                .filter(mapping -> mapping.logicalOperation.getClass().getSimpleName().equals(LOGICAL_LOAD))
                .collect(Collectors.toList());
    }

    private Graph<LogicalOperation> createLogicalGraph(List<Mapping> loadMappings, List<Mapping> mappings) {
        Multimap<String, Mapping> mappingByTags = getMappingByTag(mappings);
        MutableGraph<LogicalOperation> logicalGraph = GraphBuilder.directed().build();
        Queue<Mapping> mappingQueue = Lists.newLinkedList();
        mappingQueue.addAll(loadMappings);
        while (!mappingQueue.isEmpty()) {
            Mapping currentMapping = mappingQueue.remove();
            String outputTag = currentMapping.deserializedOperation.getOutputTag();
            Collection<Mapping> successiveMappings = mappingByTags.get(outputTag);
            mappingQueue.addAll(successiveMappings);
            successiveMappings.forEach(successiveMapping ->
                    logicalGraph.putEdge(currentMapping.logicalOperation, successiveMapping.logicalOperation));
        }
        return logicalGraph;
    }

    private Multimap<String, Mapping> getMappingByTag(List<Mapping> mappings) {
        return mappings.stream()
                .filter(mapping -> (mapping.deserializedOperation.getInputTag() != null))
                .collect(ArrayListMultimap::create,
                        (multimap, mapping) -> multimap.put(mapping.deserializedOperation.getInputTag(), mapping),
                        Multimap::putAll);
    }

    private List<LogicalLoad> getLogicalLoads(List<Mapping> loadMappings) {
        return loadMappings.stream()
                .map(loadMapping -> (LogicalLoad) loadMapping.logicalOperation)
                .collect(Collectors.toList());
    }

    @RequiredArgsConstructor
    private static class Mapping {
        private final LogicalOperation logicalOperation;
        private final DeserializedOperation deserializedOperation;
    }
}
