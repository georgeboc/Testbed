package com.testbed.interactors.converters.deserializedToLogical;

import com.testbed.entities.operations.deserialized.DeserializedJoin;
import com.testbed.entities.operations.deserialized.DeserializedOperation;
import com.testbed.entities.operations.logical.LogicalJoin;
import com.testbed.entities.operations.logical.LogicalOperation;

import javax.inject.Named;

@Named
public class JoinDeserializedToLogicalConverter implements DeserializedToLogicalConverter {
    @Override
    public LogicalOperation convert(final DeserializedOperation deserializedOperation) {
        DeserializedJoin deserializedJoin = (DeserializedJoin) deserializedOperation;
        return LogicalJoin.builder()
                .id(getId(deserializedJoin))
                .joinColumnName(deserializedJoin.getJoinColumnName())
                .build();
    }

    private String getId(final DeserializedJoin deserializedJoin) {
        return deserializedJoin.getLeftInputTag() + "_" + deserializedJoin.getRightInputTag() + "_" +
                deserializedJoin.getOutputTag();
    }
}
