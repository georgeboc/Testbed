package entities.operations.physical;

import lombok.Data;

@Data
public class PhysicalLoad implements PhysicalOperation {
    private final String datasetPath;
}
