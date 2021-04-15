package com.testbed.interactors.converters.deserializedToLogical;

import com.testbed.entities.operations.deserialized.DeserializedOperation;
import com.testbed.entities.operations.deserialized.DeserializedProject;
import com.testbed.entities.operations.logical.LogicalOperation;
import com.testbed.entities.operations.logical.LogicalProject;

import javax.inject.Named;

@Named
public class ProjectDeserializedToLogicalConverter implements DeserializedToLogicalConverter {
    private static final String OPERATION_PREFIX_ID = "operation_";

    @Override
    public LogicalOperation convert(final DeserializedOperation deserializedOperation) {
        DeserializedProject deserializedProject = (DeserializedProject) deserializedOperation;
        return LogicalProject.builder()
                .id(getId(deserializedProject))
                .columnsSelectionFactor(deserializedProject.getColumnsSelectionFactor())
                .build();
    }

    private String getId(final DeserializedProject deserializedProject) {
        return OPERATION_PREFIX_ID + deserializedProject.getInputTag() + "_" + deserializedProject.getOutputTag();
    }
}
