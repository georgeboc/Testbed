package com.testbed.interactors.converters.dispatchers;

import com.testbed.entities.operations.deserialized.DeserializedLoad;
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
