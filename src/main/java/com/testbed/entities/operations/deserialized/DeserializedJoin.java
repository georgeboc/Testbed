package com.testbed.entities.operations.deserialized;

import com.testbed.interactors.converters.dispatchers.Dispatchable;
import com.testbed.interactors.converters.dispatchers.DispatcherHandler;
import lombok.Data;

@Data
public class DeserializedJoin implements DeserializedOperation, Dispatchable, BinaryDeserializedOperation {
    private String leftInputTag;
    private String rightInputTag;
    private String outputTag;
    private String joinColumnName;

    @Override
    public Object accept(DispatcherHandler dispatcherHandler) {
        return dispatcherHandler.visit(this);
    }
}
