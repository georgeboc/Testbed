package entities.operations.logical;

import lombok.Data;

@Data
public class LogicalSelect implements LogicalOperation {
    private final double selectivityFactor;
    private final String columnName;
}
