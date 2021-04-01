package com.testbed.entities.operations.deserialized;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeserializedProject implements DeserializedOperation {
    private String inputTag;
    private String outputTag;
    private double columnsSelectionFactor;
}
