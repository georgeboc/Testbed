package com.testbed.entities.operations.deserialized;

import lombok.Data;

@Data
public class DeserializedJoin implements BinaryDeserializedOperation {
    private String leftInputTag;
    private String rightInputTag;
    private String outputTag;
    private String joinLeftColumnName;
    private String joinRightColumnName;
}
