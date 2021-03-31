package com.testbed.interactors.converters.logicalToPhysical;

import com.testbed.entities.operations.logical.LogicalLoad;
import com.testbed.entities.operations.physical.PhysicalLoad;
import com.testbed.entities.operations.physical.PhysicalOperation;
import com.testbed.entities.profiles.ProfileEstimation;

public class LogicalToPhysicalLoadConverter implements LogicalToPhysicalOperationConverter {
    @Override
    public PhysicalOperation convert(final ProfileEstimation profileEstimation) {
        LogicalLoad logicalLoad = (LogicalLoad) profileEstimation.getLogicalOperation();
        return new PhysicalLoad(logicalLoad.getDatasetDirectoryPath());
    }
}
