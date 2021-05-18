package com.testbed.entities.operations.deserialized;

import lombok.Data;

@Data
public class DeserializedProject implements UnaryDeserializedOperation {
    private String inputTag;
    private String outputTag;
    private Double columnSelectivityFactor;
}
