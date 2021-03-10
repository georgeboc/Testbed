package interactors.converters.deserializedToLogical;

import entities.operations.deserialized.DeserializedOperation;
import entities.operations.deserialized.DeserializedSelect;
import entities.operations.logical.LogicalOperation;
import entities.operations.logical.LogicalSelect;

public class DeserializedSelectConverter implements DeserializedOperationConverter {
    @Override
    public LogicalOperation convert(DeserializedOperation deserializedOperation) {
        DeserializedSelect deserializedSelect = (DeserializedSelect) deserializedOperation;
        return new LogicalSelect(deserializedSelect.getSelectivityFactor(), deserializedSelect.getColumnName());
    }
}
