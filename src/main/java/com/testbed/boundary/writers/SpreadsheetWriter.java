package com.testbed.boundary.writers;

import com.testbed.entities.parameters.OutputParameters;

public interface SpreadsheetWriter {
    void addFormula(final OutputParameters outputParameters, final Position position, final String formula);
    void write(final OutputParameters outputParameters, final Position position, final String value);
    void writeWithColor(final OutputParameters outputParameters, final Position position, final String value, final String colorName);
    boolean isEmpty(final OutputParameters outputParameters, final Position position);
    int getFirstUnwrittenColumn(OutputParameters outputParameters, int row, int columnOffset);
    void makeMergedRegion(final OutputParameters outputParameters, final Position startPosition, final Position endPosition);
    void removeSheet(final OutputParameters outputParameters);
}
