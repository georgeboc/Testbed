package com.testbed.entities.operations.deserialized;

import lombok.Data;

@Data
public class DeserializedGroupby implements UnaryDeserializedOperation {
    private String inputTag;
    private String outputTag;
}
