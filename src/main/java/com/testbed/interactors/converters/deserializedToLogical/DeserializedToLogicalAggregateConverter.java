package com.testbed.interactors.converters.deserializedToLogical;

import com.testbed.entities.operations.deserialized.DeserializedAggregate;
import com.testbed.entities.operations.deserialized.DeserializedOperation;
import com.testbed.entities.operations.logical.LogicalAggregate;
import com.testbed.entities.operations.logical.LogicalOperation;

public class DeserializedToLogicalAggregateConverter implements DeserializedToLogicalOperationConverter {
    @Override
    public LogicalOperation convert(final DeserializedOperation deserializedOperation) {
        DeserializedAggregate deserializedAggregate = (DeserializedAggregate) deserializedOperation;
        return LogicalAggregate.builder()
                ._id(getId(deserializedAggregate))
                .aggregationColumnName(deserializedAggregate.getAggregationColumnName())
                .aggregationOperation(deserializedAggregate.getAggregationOperation())
                .build();
    }

    private String getId(DeserializedAggregate deserializedAggregate) {
        return deserializedAggregate.getInputTag() + "_" + deserializedAggregate.getOutputTag();
    }
}
