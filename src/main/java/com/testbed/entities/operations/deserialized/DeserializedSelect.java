package com.testbed.entities.operations.deserialized;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeserializedSelect implements DeserializedOperation {
    private String inputTag;
    private String outputTag;
    private double selectivityFactor;
    private String columnName;
}
