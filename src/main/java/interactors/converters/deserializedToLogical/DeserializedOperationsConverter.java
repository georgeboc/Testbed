package interactors.converters.deserializedToLogical;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
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
public class DeserializedOperationsConverter {
    private final static Logger LOG = Logger.getLogger(DeserializedOperationsConverter.class.getName());
    private static final String LOGICAL_LOAD = "LogicalLoad";
    private final Map<String, DeserializedOperationConverter> deserializedOperationConverterMapping;

    public LogicalPlan convert(DeserializedOperations deserializedOperations) {
        LOG.info("DeserializedOperations: " + deserializedOperations);
        List<Mapping> mappings = getMappings(deserializedOperations);
        LOG.info("Mappings: " + mappings);
        List<Mapping> loadMappings = getLoadMappings(mappings);
        LOG.info("LoadMappings: " + loadMappings);
        Multimap<LogicalOperation, LogicalOperation> graph = createLogicalGraph(loadMappings, mappings);
        LOG.info("Graph generated: " + graph);
        List<LogicalLoad> logicalLoads = getLogicalLoads(loadMappings);
        LOG.info("LoadOperations: " + logicalLoads);
        return LogicalPlan.builder()
                .graph(graph)
                .logicalLoads(logicalLoads)
                .build();
    }

    private List<Mapping> getMappings(DeserializedOperations deserializedOperations) {
        List<Mapping> mappings = Lists.newLinkedList();
        for (DeserializedOperation deserializedOperation: deserializedOperations) {
            DeserializedOperationConverter deserializedOperationConverter = deserializedOperationConverterMapping
                    .get(deserializedOperation.getClass().getSimpleName());
            LogicalOperation logicalOperation = deserializedOperationConverter.convert(deserializedOperation);
            mappings.add(new Mapping(logicalOperation, deserializedOperation));
        }
        return mappings;
    }

    private List<Mapping> getLoadMappings(List<Mapping> mappings) {
        return mappings.stream()
                .filter(mapping -> mapping.logicalOperation.getClass().getSimpleName().equals(LOGICAL_LOAD))
                .collect(Collectors.toList());
    }

    private Multimap<LogicalOperation, LogicalOperation> createLogicalGraph(List<Mapping> loadMappings,
                                                                            List<Mapping> mappings) {
        Multimap<String, Mapping> mappingByTags = getMappingByTag(mappings);
        LOG.info("Mapping by input tags: " + mappingByTags);
        Multimap<LogicalOperation, LogicalOperation> logicalGraph = MultimapBuilder.hashKeys().arrayListValues().build();
        Queue<Mapping> mappingQueue = Lists.newLinkedList();
        mappingQueue.addAll(loadMappings);
        while (!mappingQueue.isEmpty()) {
            Mapping currentMapping = mappingQueue.remove();
            String outputTag = currentMapping.deserializedOperation.getOutputTag();
            Collection<Mapping> adjacentMappings = mappingByTags.get(outputTag);
            for (Mapping adjacentMapping: adjacentMappings) {
                logicalGraph.put(currentMapping.logicalOperation, adjacentMapping.logicalOperation);
                mappingQueue.add(adjacentMapping);
            }
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
