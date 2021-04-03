package com.testbed.interactors.converters.deserializedToLogical;

import com.testbed.entities.operations.deserialized.DeserializedLoad;
import com.testbed.entities.operations.deserialized.DeserializedOperation;
import com.testbed.entities.operations.logical.LogicalLoad;
import com.testbed.entities.operations.logical.LogicalOperation;

public class DeserializedToLogicalLoadConverter implements DeserializedToLogicalOperationConverter {
    @Override
    public LogicalOperation convert(final DeserializedOperation deserializedOperation) {
        DeserializedLoad deserializedLoad = (DeserializedLoad) deserializedOperation;
        return LogicalLoad.builder()
                ._id(getId(deserializedLoad))
                .datasetDirectoryPath(deserializedLoad.getDatasetDirectoryPath())
                .build();
    }

    private String getId(DeserializedLoad deserializedLoad) {
        return deserializedLoad.getOutputTag();
    }
}
