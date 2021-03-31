package com.testbed.boundary.readers;

import com.testbed.entities.profiles.ColumnProfile;

public interface ColumnReader {
    String getValueFromSelectivityFactor(final double selectivityFactor,
                                         final ColumnProfile columnProfile,
                                         final String columnName,
                                         final String filePath);
}
