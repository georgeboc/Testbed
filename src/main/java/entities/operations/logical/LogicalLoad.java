package entities.operations.logical;

import lombok.Data;

@Data
public class LogicalLoad implements LogicalOperation {
    private final String datasetDirectoryPath;
}
