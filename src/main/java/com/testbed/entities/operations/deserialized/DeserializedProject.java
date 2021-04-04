package com.testbed.entities.operations.deserialized;

import com.testbed.interactors.dispatchers.Dispatchable;
import com.testbed.interactors.dispatchers.DispatcherManager;
import lombok.Data;

@Data
public class DeserializedProject implements Dispatchable, UnaryDeserializedOperation {
    private String inputTag;
    private String outputTag;
    private double columnsSelectionFactor;

    @Override
    public Object accept(DispatcherManager dispatcherManager) {
        return dispatcherManager.visit(this);
    }
}
