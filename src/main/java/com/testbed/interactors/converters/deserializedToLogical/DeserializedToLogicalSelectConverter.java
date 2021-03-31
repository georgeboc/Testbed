package com.testbed.interactors.converters.deserializedToLogical;

import com.testbed.entities.operations.deserialized.DeserializedOperation;
import com.testbed.entities.operations.deserialized.DeserializedSelect;
import com.testbed.entities.operations.logical.LogicalOperation;
import com.testbed.entities.operations.logical.LogicalSelect;

public class DeserializedToLogicalSelectConverter implements DeserializedToLogicalOperationConverter {
    @Override
    public LogicalOperation convert(final DeserializedOperation deserializedOperation) {
        DeserializedSelect deserializedSelect = (DeserializedSelect) deserializedOperation;
        return new LogicalSelect(deserializedSelect.getSelectivityFactor(), deserializedSelect.getColumnName());
    }
}
