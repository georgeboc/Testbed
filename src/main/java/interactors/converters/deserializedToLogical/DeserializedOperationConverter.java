package interactors.converters.deserializedToLogical;

import entities.operations.deserialized.DeserializedOperation;
import entities.operations.logical.LogicalOperation;

public interface DeserializedOperationConverter {
    LogicalOperation convert(DeserializedOperation deserializedOperation);
}
