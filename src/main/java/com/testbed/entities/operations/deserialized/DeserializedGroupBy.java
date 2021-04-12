package com.testbed.entities.operations.deserialized;

import lombok.Data;

import java.util.List;

@Data
public class DeserializedGroupBy implements UnaryDeserializedOperation {
    private String inputTag;
    private String outputTag;
    private List<String> groupingColumnNames;
}
