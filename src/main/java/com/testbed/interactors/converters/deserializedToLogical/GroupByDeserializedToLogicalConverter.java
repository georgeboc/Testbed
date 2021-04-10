package com.testbed.interactors.converters.deserializedToLogical;

import com.testbed.entities.operations.deserialized.DeserializedGroupby;
import com.testbed.entities.operations.deserialized.DeserializedOperation;
import com.testbed.entities.operations.logical.LogicalGroupby;
import com.testbed.entities.operations.logical.LogicalOperation;

import javax.inject.Named;

@Named
public class GroupByDeserializedToLogicalConverter implements DeserializedToLogicalConverter {
    @Override
    public LogicalOperation convert(final DeserializedOperation deserializedOperation) {
        DeserializedGroupby deserializedGroupBy = (DeserializedGroupby) deserializedOperation;
        return LogicalGroupby.builder()
                .id(getId(deserializedGroupBy))
                .groupingColumnNames(deserializedGroupBy.getGroupingColumnNames())
                .build();
    }

    private String getId(final DeserializedGroupby deserializedGroupBy) {
        return deserializedGroupBy.getInputTag() + "_" + deserializedGroupBy.getOutputTag();
    }
}
