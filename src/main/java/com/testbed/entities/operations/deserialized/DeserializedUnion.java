package com.testbed.entities.operations.deserialized;

import com.testbed.interactors.dispatchers.Dispatchable;
import com.testbed.interactors.dispatchers.DispatcherManager;
import lombok.Data;

@Data
public class DeserializedUnion implements Dispatchable, BinaryDeserializedOperation {
    private String leftInputTag;
    private String rightInputTag;
    private String outputTag;

    @Override
    public Object accept(DispatcherManager dispatcherManager) {
        return dispatcherManager.visit(this);
    }
}
