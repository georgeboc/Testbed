package com.testbed.interactors.converters.deserializedToLogical;

import com.testbed.entities.operations.deserialized.DeserializedLoad;
import com.testbed.entities.operations.deserialized.DeserializedOperation;
import com.testbed.entities.operations.logical.LogicalLoad;
import com.testbed.entities.operations.logical.LogicalOperation;

public class LoadDeserializedToLogicalConverter implements DeserializedToLogicalConverter {
    private static final String OPERATION_PREFIX_ID = "operation_";

    @Override
    public LogicalOperation convert(final DeserializedOperation deserializedOperation) {
        DeserializedLoad deserializedLoad = (DeserializedLoad) deserializedOperation;
        return LogicalLoad.builder()
                .id(getId(deserializedLoad))
                .datasetDirectoryPath(deserializedLoad.getDatasetDirectoryPath())
                .build();
    }

    private String getId(final DeserializedLoad deserializedLoad) {
        return OPERATION_PREFIX_ID + deserializedLoad.getOutputTag();
    }
}
