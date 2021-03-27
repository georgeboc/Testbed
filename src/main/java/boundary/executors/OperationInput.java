package boundary.executors;

import entities.operations.physical.PhysicalOperation;
import lombok.Builder;
import lombok.Data;

import java.util.Collection;

@Data
@Builder
public class OperationInput {
    private final Collection<Result> inputResults;
    private final PhysicalOperation physicalOperation;
}

