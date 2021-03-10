package interactors.converters.logicalToPhysical;

import entities.operations.logical.LogicalLoad;
import entities.operations.logical.LogicalOperation;
import entities.operations.physical.PhysicalLoad;
import entities.operations.physical.PhysicalOperation;

public class LogicalLoadConverter implements LogicalOperationConverter {
    @Override
    public PhysicalOperation convert(LogicalOperation logicalOperation) {
        LogicalLoad logicalLoad = (LogicalLoad) logicalOperation;
        return new PhysicalLoad(logicalLoad.getDatasetPath());
    }
}
