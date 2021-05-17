package com.testbed.boundary.readers;

public interface ColumnReader {
    String getValueFromSelectivityFactor(final double selectivityFactor,
                                         final long columnDistinctRowsCount,
                                         final String columnName,
                                         final String filePath);
}
