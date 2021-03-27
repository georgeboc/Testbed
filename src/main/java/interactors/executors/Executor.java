package interactors.executors;

import boundary.executors.Executable;
import boundary.executors.OperationInput;
import boundary.executors.Result;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.Streams;
import entities.operations.physical.PhysicalOperation;
import entities.operations.physical.PhysicalPlan;
import interactors.converters.deserializedToLogical.DeserializedOperationsConverter;
import lombok.RequiredArgsConstructor;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class Executor {
    private final static Logger LOG = Logger.getLogger(DeserializedOperationsConverter.class.getName());
    private final TopologicalSorter topologicalSorter;
    private final Map<String, Executable> executableMapper;

    public void execute(PhysicalPlan physicalPlan) {
        List<PhysicalOperation> topologicallySortedPhysicalOperations = topologicalSorter.sortTopologically(physicalPlan);
        Multimap<PhysicalOperation, Result> physicalOperationInputResultsMap = MultimapBuilder.hashKeys().arrayListValues().build();
        Stream<Executable> executableStream = topologicallySortedPhysicalOperations.stream()
                .map(physicalOperation -> physicalOperation.getClass().getSimpleName())
                .map(executableMapper::get);
        Stream<OperationInput> operationInputStream = topologicallySortedPhysicalOperations.stream()
                .map(physicalOperation -> createOperationInput(physicalOperationInputResultsMap, physicalOperation));
        Stream<Result> executionResultStream = Streams.zip(operationInputStream, executableStream,
                (operationInput, executable) -> executable.execute(operationInput));

        Streams.zip(topologicallySortedPhysicalOperations.stream(),
                executionResultStream,
                (physicalOperation, result) -> physicalPlan.getGraph().get(physicalOperation).stream()
                        .map(predecessorPhysicalOperation -> new AbstractMap.SimpleEntry<>(predecessorPhysicalOperation, result)))
                .flatMap(Function.identity())
                .forEach(entry -> physicalOperationInputResultsMap.put(entry.getKey(), entry.getValue()));

        LOG.info("Physical operation and Input results Map: " + physicalOperationInputResultsMap.toString());
    }

    private OperationInput createOperationInput(Multimap<PhysicalOperation, Result> intermediateResults,
                                    PhysicalOperation physicalOperation) {
        return OperationInput.builder()
                .physicalOperation(physicalOperation)
                .inputResults(intermediateResults.get(physicalOperation))
                .build();
    }


}
