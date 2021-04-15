package com.testbed.interactors.converters.deserializedToLogical;

import com.testbed.entities.operations.deserialized.DeserializedOperation;
import com.testbed.entities.operations.deserialized.DeserializedUnion;
import com.testbed.entities.operations.logical.LogicalOperation;
import com.testbed.entities.operations.logical.LogicalUnion;

import javax.inject.Named;

@Named
public class UnionDeserializedToLogicalConverter implements DeserializedToLogicalConverter {
    private static final String OPERATION_PREFIX_ID = "operation_";

    @Override
    public LogicalOperation convert(final DeserializedOperation deserializedOperation) {
        DeserializedUnion deserializedUnion = (DeserializedUnion) deserializedOperation;
        return new LogicalUnion(getId(deserializedUnion));
    }

    private String getId(final DeserializedUnion deserializedUnion) {
        return OPERATION_PREFIX_ID + deserializedUnion.getLeftInputTag() + "_" +
                deserializedUnion.getRightInputTag() + "_" + deserializedUnion.getOutputTag();
    }
}
