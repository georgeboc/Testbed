package entities.exceptions;

public class ColumnNotFoundException extends RuntimeException {
    public ColumnNotFoundException(String columnName) {
        super("Column " + columnName + " not found");
    }
}
