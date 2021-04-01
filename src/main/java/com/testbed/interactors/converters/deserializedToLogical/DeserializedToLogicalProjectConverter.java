package com.testbed.interactors.converters.deserializedToLogical;

import com.testbed.entities.operations.deserialized.DeserializedOperation;
import com.testbed.entities.operations.deserialized.DeserializedProject;
import com.testbed.entities.operations.logical.LogicalOperation;
import com.testbed.entities.operations.logical.LogicalProject;

public class DeserializedToLogicalProjectConverter implements DeserializedToLogicalOperationConverter {
    @Override
    public LogicalOperation convert(final DeserializedOperation deserializedOperation) {
        DeserializedProject deserializedProject = (DeserializedProject) deserializedOperation;
        return new LogicalProject(deserializedProject.getColumnsSelectionFactor());
    }
}
