package entities.operations.physical;

import lombok.Data;

@Data
public class PhysicalSelect implements PhysicalOperation {
    private final String lessThanValue;
    private final String columnName;
}
