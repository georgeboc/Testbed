package com.testbed.interactors.converters.deserializedToLogical;

import com.testbed.entities.operations.deserialized.DeserializedOperation;
import com.testbed.entities.operations.logical.LogicalOperation;

public interface DeserializedToLogicalOperationConverter {
    LogicalOperation convert(DeserializedOperation deserializedOperation);
}
