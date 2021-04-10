package com.testbed.entities.operations.deserialized;

import lombok.Data;

@Data
public class DeserializedSelect implements UnaryDeserializedOperation {
    private String inputTag;
    private String outputTag;
    private Double selectivityFactor;
    private String columnName;
}
