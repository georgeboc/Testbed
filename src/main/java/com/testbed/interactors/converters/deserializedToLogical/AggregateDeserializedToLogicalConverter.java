package com.testbed.interactors.converters.deserializedToLogical;

import com.testbed.entities.operations.deserialized.DeserializedAggregate;
import com.testbed.entities.operations.deserialized.DeserializedOperation;
import com.testbed.entities.operations.logical.LogicalAggregate;
import com.testbed.entities.operations.logical.LogicalOperation;

import javax.inject.Named;

@Named
public class AggregateDeserializedToLogicalConverter implements DeserializedToLogicalConverter {
    @Override
    public LogicalOperation convert(final DeserializedOperation deserializedOperation) {
        DeserializedAggregate deserializedAggregate = (DeserializedAggregate) deserializedOperation;
        return LogicalAggregate.builder()
                .id(getId(deserializedAggregate))
                .aggregationColumnName(deserializedAggregate.getAggregationColumnName())
                .aggregationOperation(deserializedAggregate.getAggregationOperation())
                .build();
    }

    private String getId(final DeserializedAggregate deserializedAggregate) {
        return deserializedAggregate.getInputTag() + "_" + deserializedAggregate.getOutputTag();
    }
}
