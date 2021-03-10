package interactors.converters.logicalToPhysical;

import boundary.deserializers.DatasetProfileDeserializer;
import boundary.readers.Reader;
import entities.operations.logical.LogicalLoad;
import entities.operations.logical.LogicalPlan;
import entities.operations.physical.PhysicalPlan;
import entities.profiles.DatasetProfile;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class LogicalOperationsConverter {
    private final Reader reader;
    private final DatasetProfileDeserializer datasetProfileDeserializer;
    private final Map<String, LogicalOperationConverter> logicalOperationConverterMapping;

    public PhysicalPlan convert(LogicalPlan logicalPlan) {
        List<LogicalLoad> loadOperations = logicalPlan.getLoadOperations();
        List<DatasetProfile> datasetProfiles = getDatasetProfiles(loadOperations);
        return null;
    }

    private List<DatasetProfile> getDatasetProfiles(List<LogicalLoad> loadOperations) {
        return loadOperations.stream()
                .map(logicalLoad -> datasetProfileDeserializer.deserialize(reader.read(logicalLoad.getDatasetPath())))
                .collect(Collectors.toList());
    }
}
