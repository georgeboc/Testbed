package interactors.converters.logicalToPhysical;

import boundary.deserializers.ProfileDeserializer;
import entities.operations.logical.LogicalPlan;
import entities.operations.physical.PhysicalPlan;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class LogicalOperationsConverter {
    private final ProfileDeserializer profileDeserializer;
    private final Map<String, LogicalOperationConverter> logicalOperationConverterMapping;

    public PhysicalPlan convert(LogicalPlan logicalPlan) {
        return null;
    }
}
