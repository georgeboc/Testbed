package com.testbed.boundary.readers;

public interface ColumnReader {
    String getValueFromRowId(final long rowId, final String columnName, final String filePath);
}
