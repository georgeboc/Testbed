package com.testbed.boundary.readers;

public interface ColumnReader {
    String getValueFromRowId(long rowId, String columnName, String filePath);
}