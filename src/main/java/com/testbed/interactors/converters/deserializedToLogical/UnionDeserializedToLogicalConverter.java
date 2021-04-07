package com.testbed.interactors.converters.deserializedToLogical;

import com.testbed.entities.operations.deserialized.DeserializedOperation;
import com.testbed.entities.operations.deserialized.DeserializedUnion;
import com.testbed.entities.operations.logical.LogicalOperation;
import com.testbed.entities.operations.logical.LogicalUnion;

import javax.inject.Named;

@Named
public class UnionDeserializedToLogicalConverter implements DeserializedToLogicalConverter {
    @Override
    public LogicalOperation convert(final DeserializedOperation deserializedOperation) {
        DeserializedUnion deserializedUnion = (DeserializedUnion) deserializedOperation;
        return new LogicalUnion(getId(deserializedUnion));
    }

    private String getId(final DeserializedUnion deserializedUnion) {
        return deserializedUnion.getLeftInputTag() + "_" + deserializedUnion.getRightInputTag() + "_" +
                deserializedUnion.getOutputTag();
    }
}
