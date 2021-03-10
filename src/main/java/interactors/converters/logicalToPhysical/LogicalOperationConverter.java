package interactors.converters.logicalToPhysical;

import entities.operations.logical.LogicalOperation;
import entities.operations.physical.PhysicalOperation;

public interface LogicalOperationConverter {
    PhysicalOperation convert(LogicalOperation logicalOperation);
}
