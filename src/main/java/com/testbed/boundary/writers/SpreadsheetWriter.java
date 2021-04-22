package com.testbed.boundary.writers;

import com.testbed.entities.parameters.OutputParameters;

import java.util.List;

public interface SpreadsheetWriter {
    void addHeaders(final OutputParameters outputParameters, final List<String> headers, final String colorName);
    void addFormula(final OutputParameters outputParameters, final Position position, final String formula);
    void write(final OutputParameters outputParameters, final Position position, final String value);
    boolean isEmpty(final OutputParameters outputParameters, final Position position);
    int getFirstUnwrittenRow(OutputParameters outputParameters, int column);
    int getFirstUnwrittenColumn(OutputParameters outputParameters, int row);
}
