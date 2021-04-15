package com.testbed.interactors.converters.deserializedToLogical;

import com.testbed.entities.operations.deserialized.DeserializedGroupBy;
import com.testbed.entities.operations.deserialized.DeserializedOperation;
import com.testbed.entities.operations.logical.LogicalGroupBy;
import com.testbed.entities.operations.logical.LogicalOperation;

import javax.inject.Named;

@Named
public class GroupByDeserializedToLogicalConverter implements DeserializedToLogicalConverter {
    private static final String OPERATION_PREFIX_ID = "operation_";

    @Override
    public LogicalOperation convert(final DeserializedOperation deserializedOperation) {
        DeserializedGroupBy deserializedGroupBy = (DeserializedGroupBy) deserializedOperation;
        return LogicalGroupBy.builder()
                .id(getId(deserializedGroupBy))
                .groupingColumnNames(deserializedGroupBy.getGroupingColumnNames())
                .build();
    }

    private String getId(final DeserializedGroupBy deserializedGroupBy) {
        return OPERATION_PREFIX_ID + deserializedGroupBy.getInputTag() + "_" + deserializedGroupBy.getOutputTag();
    }
}
