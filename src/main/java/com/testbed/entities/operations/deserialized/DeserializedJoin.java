package com.testbed.entities.operations.deserialized;

import com.testbed.interactors.converters.dispatchers.Dispatchable;
import com.testbed.interactors.converters.dispatchers.DispatcherHandler;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeserializedJoin implements DeserializedOperation, Dispatchable {
    private String firstInputTag;
    private String secondInputTag;
    private String outputTag;
    private String joinColumnName;

    @Override
    public Object accept(DispatcherHandler dispatcherHandler) {
        return dispatcherHandler.visit(this);
    }
}
