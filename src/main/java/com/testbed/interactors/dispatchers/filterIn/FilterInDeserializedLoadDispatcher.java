package com.testbed.interactors.dispatchers.filterIn;

import com.testbed.entities.operations.deserialized.DeserializedLoad;
import com.testbed.interactors.dispatchers.Dispatcher;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FilterInDeserializedLoadDispatcher implements Dispatcher<DeserializedLoad, DeserializedLoad> {
    @Override
    public DeserializedLoad dispatch(Object object) {
        return (DeserializedLoad) object;
    }
}
