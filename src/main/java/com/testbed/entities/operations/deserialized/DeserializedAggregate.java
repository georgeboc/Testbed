package com.testbed.entities.operations.deserialized;

import com.testbed.interactors.converters.dispatchers.Dispatchable;
import com.testbed.interactors.converters.dispatchers.DispatcherHandler;
import lombok.Data;

@Data
public class DeserializedAggregate implements DeserializedOperation, Dispatchable {
    private String inputTag;
    private String outputTag;
    private String aggregationColumnName;
    private String aggregationOperation;

    @Override
    public Object accept(DispatcherHandler dispatcherHandler) {
        return dispatcherHandler.visit(this);
    }
}
