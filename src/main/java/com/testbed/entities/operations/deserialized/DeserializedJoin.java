package com.testbed.entities.operations.deserialized;

import com.testbed.interactors.dispatchers.Dispatchable;
import com.testbed.interactors.dispatchers.DispatcherManager;
import lombok.Data;

@Data
public class DeserializedJoin implements Dispatchable, BinaryDeserializedOperation {
    private String leftInputTag;
    private String rightInputTag;
    private String outputTag;
    private String joinColumnName;

    @Override
    public Object accept(DispatcherManager dispatcherManager) {
        return dispatcherManager.visit(this);
    }
}
