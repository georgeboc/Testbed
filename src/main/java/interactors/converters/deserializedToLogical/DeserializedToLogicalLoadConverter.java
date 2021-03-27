package interactors.converters.deserializedToLogical;

import entities.operations.deserialized.DeserializedLoad;
import entities.operations.deserialized.DeserializedOperation;
import entities.operations.logical.LogicalLoad;
import entities.operations.logical.LogicalOperation;

public class DeserializedToLogicalLoadConverter implements DeserializedToLogicalOperationConverter {
    @Override
    public LogicalOperation convert(DeserializedOperation deserializedOperation) {
        DeserializedLoad deserializedLoad = (DeserializedLoad) deserializedOperation;
        return new LogicalLoad(deserializedLoad.getDatasetDirectoryPath());
    }
}
