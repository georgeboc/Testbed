package interactors.converters.logicalToPhysical;

import entities.operations.logical.LogicalLoad;
import entities.operations.physical.PhysicalLoad;
import entities.operations.physical.PhysicalOperation;
import entities.profiles.ProfileEstimation;

public class LogicalLoadConverter implements LogicalOperationConverter {
    @Override
    public PhysicalOperation convert(ProfileEstimation profileEstimation) {
        LogicalLoad logicalLoad = (LogicalLoad) profileEstimation.getLogicalOperation();
        return new PhysicalLoad(logicalLoad.getDatasetDirectoryPath());
    }
}
