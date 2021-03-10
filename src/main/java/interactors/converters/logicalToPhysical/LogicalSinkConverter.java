package interactors.converters.logicalToPhysical;

import entities.operations.logical.LogicalOperation;
import entities.operations.physical.PhysicalOperation;
import entities.operations.physical.PhysicalSink;

public class LogicalSinkConverter implements LogicalOperationConverter {
    @Override
    public PhysicalOperation convert(LogicalOperation logicalOperation) {
        return new PhysicalSink();
    }
}
