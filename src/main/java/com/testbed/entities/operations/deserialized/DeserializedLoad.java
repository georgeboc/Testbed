package com.testbed.entities.operations.deserialized;

import com.testbed.interactors.dispatchers.Dispatchable;
import com.testbed.interactors.dispatchers.DispatcherManager;
import lombok.Data;

@Data
public class DeserializedLoad implements Dispatchable, ZeroaryDeserializedOperation {
    private String datasetDirectoryPath;
    private String outputTag;

    @Override
    public Object accept(DispatcherManager dispatcherManager) {
        return dispatcherManager.visit(this);
    }
}
