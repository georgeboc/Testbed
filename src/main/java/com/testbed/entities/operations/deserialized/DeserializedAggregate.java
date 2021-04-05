package com.testbed.entities.operations.deserialized;

import com.testbed.interactors.dispatchers.Dispatchable;
import com.testbed.interactors.dispatchers.DispatcherManager;
import lombok.Data;

@Data
public class DeserializedAggregate implements Dispatchable, UnaryDeserializedOperation {
    private String inputTag;
    private String outputTag;
    private String aggregationColumnName;
    private String aggregationOperation;

    @Override
    public Object accept(DispatcherManager dispatcherManager) {
        return dispatcherManager.visit(this);
    }
}
