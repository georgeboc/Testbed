package com.testbed.entities.operations.deserialized;

import lombok.Data;

@Data
public class DeserializedAggregate implements UnaryDeserializedOperation {
    private String inputTag;
    private String outputTag;
    private String aggregationColumnName;
    private String aggregationOperation;
}
