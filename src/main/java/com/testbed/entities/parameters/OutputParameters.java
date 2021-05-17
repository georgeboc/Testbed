package com.testbed.entities.parameters;

import lombok.Data;

@Data
public class OutputParameters {
    private String outputPath;
    private String sheetName;
    private boolean isOverwriteSheetEnabled;
}
