package com.testbed.interactors.converters.deserializedToLogical;

import com.testbed.entities.operations.deserialized.DeserializedGroupBy;
import com.testbed.entities.operations.deserialized.DeserializedOperation;
import com.testbed.entities.operations.logical.LogicalGroupBy;
import com.testbed.entities.operations.logical.LogicalOperation;

import javax.inject.Named;

@Named
public class GroupByDeserializedToLogicalConverter implements DeserializedToLogicalConverter {
    @Override
    public LogicalOperation convert(final DeserializedOperation deserializedOperation) {
        DeserializedGroupBy deserializedGroupBy = (DeserializedGroupBy) deserializedOperation;
        return new LogicalGroupBy(getId(deserializedGroupBy));
    }

    private String getId(final DeserializedGroupBy deserializedGroupBy) {
        return deserializedGroupBy.getInputTag() + "_" + deserializedGroupBy.getOutputTag();
    }
}
