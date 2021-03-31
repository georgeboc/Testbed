package com.testbed.entities.exceptions;

public class ColumnNotFoundException extends RuntimeException {
    public ColumnNotFoundException(final String columnName) {
        super("Column " + columnName + " not found");
    }
}
