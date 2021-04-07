package com.testbed.interactors.converters.deserializedToLogical;

import com.testbed.entities.operations.deserialized.DeserializedOperation;
import com.testbed.entities.operations.deserialized.DeserializedSelect;
import com.testbed.entities.operations.logical.LogicalOperation;
import com.testbed.entities.operations.logical.LogicalSelect;

import javax.inject.Named;

@Named
public class SelectDeserializedToLogicalConverter implements DeserializedToLogicalConverter {
    @Override
    public LogicalOperation convert(final DeserializedOperation deserializedOperation) {
        DeserializedSelect deserializedSelect = (DeserializedSelect) deserializedOperation;
        return LogicalSelect.builder()
                .id(getId(deserializedSelect))
                .selectivityFactor(deserializedSelect.getSelectivityFactor())
                .columnName(deserializedSelect.getColumnName())
                .build();
    }

    private String getId(final DeserializedSelect deserializedSelect) {
        return deserializedSelect.getInputTag() + "_" + deserializedSelect.getOutputTag();
    }
}
