package com.testbed.interactors.converters.logicalToPhysical;

import com.testbed.entities.operations.logical.LogicalLoad;
import com.testbed.entities.operations.physical.PhysicalLoad;
import com.testbed.entities.operations.physical.PhysicalOperation;
import com.testbed.entities.profiles.ProfileEstimation;

public class LoadLogicalToPhysicalConverter implements LogicalToPhysicalConverter {
    @Override
    public PhysicalOperation convert(final ProfileEstimation profileEstimation) {
        LogicalLoad logicalLoad = (LogicalLoad) profileEstimation.getLogicalOperation();
        return PhysicalLoad.builder()
                ._id(logicalLoad.get_id())
                .datasetDirectoryPath(logicalLoad.getDatasetDirectoryPath())
                .build();
    }
}
