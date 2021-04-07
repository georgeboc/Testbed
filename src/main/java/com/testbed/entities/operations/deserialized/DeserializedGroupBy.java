package com.testbed.entities.operations.deserialized;

import lombok.Data;

@Data
public class DeserializedGroupBy implements UnaryDeserializedOperation {
    private String inputTag;
    private String outputTag;
}
