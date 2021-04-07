package com.testbed.entities.operations.deserialized;

import lombok.Data;

@Data
public class DeserializedUnion implements BinaryDeserializedOperation {
    private String leftInputTag;
    private String rightInputTag;
    private String outputTag;
}
