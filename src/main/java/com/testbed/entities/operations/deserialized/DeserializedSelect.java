package com.testbed.entities.operations.deserialized;

import com.testbed.interactors.converters.dispatchers.Dispatchable;
import com.testbed.interactors.converters.dispatchers.DispatcherHandler;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeserializedSelect implements DeserializedOperation, Dispatchable {
    private String inputTag;
    private String outputTag;
    private double selectivityFactor;
    private String columnName;

    @Override
    public Object accept(DispatcherHandler dispatcherHandler) {
        return dispatcherHandler.visit(this);
    }
}
