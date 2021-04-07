package com.testbed.entities.operations.deserialized;

import lombok.Data;

@Data
public class DeserializedLoad implements DeserializedOperation {
    private String datasetDirectoryPath;
    private String outputTag;
}
