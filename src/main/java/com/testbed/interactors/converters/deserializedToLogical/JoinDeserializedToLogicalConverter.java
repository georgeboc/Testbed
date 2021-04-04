package com.testbed.interactors.converters.deserializedToLogical;

import com.testbed.entities.operations.deserialized.DeserializedJoin;
import com.testbed.entities.operations.deserialized.DeserializedOperation;
import com.testbed.entities.operations.logical.LogicalJoin;
import com.testbed.entities.operations.logical.LogicalOperation;

public class JoinDeserializedToLogicalConverter implements DeserializedToLogicalConverter {
    @Override
    public LogicalOperation convert(DeserializedOperation deserializedOperation) {
        DeserializedJoin deserializedJoin = (DeserializedJoin) deserializedOperation;
        return LogicalJoin.builder()
                ._id(getId(deserializedJoin))
                .joinColumnName(deserializedJoin.getJoinColumnName())
                .build();
    }

    private String getId(DeserializedJoin deserializedJoin) {
        return deserializedJoin.getLeftInputTag() + "_" + deserializedJoin.getRightInputTag() + "_" +
                deserializedJoin.getOutputTag();
    }
}
