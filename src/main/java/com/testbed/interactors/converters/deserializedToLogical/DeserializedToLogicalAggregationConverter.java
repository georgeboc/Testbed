package com.testbed.interactors.converters.deserializedToLogical;

import com.testbed.entities.operations.deserialized.DeserializedAggregation;
import com.testbed.entities.operations.deserialized.DeserializedOperation;
import com.testbed.entities.operations.logical.LogicalAggregation;
import com.testbed.entities.operations.logical.LogicalOperation;

public class DeserializedToLogicalAggregationConverter implements DeserializedToLogicalOperationConverter {
    @Override
    public LogicalOperation convert(final DeserializedOperation deserializedOperation) {
        DeserializedAggregation deserializedAggregation = (DeserializedAggregation) deserializedOperation;
        return LogicalAggregation.builder()
                ._id(getId(deserializedAggregation))
                .aggregationColumnName(deserializedAggregation.getAggregationColumnName())
                .operation(deserializedAggregation.getOperation())
                .build();
    }

    private String getId(DeserializedAggregation deserializedAggregation) {
        return deserializedAggregation.getInputTag() + "_" + deserializedAggregation.getOutputTag();
    }
}
