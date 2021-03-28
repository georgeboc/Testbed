package com.testbed.interactors.converters.logicalToPhysical;

import com.testbed.entities.exceptions.ColumnNotFoundException;
import com.testbed.entities.operations.physical.PhysicalOperation;
import com.testbed.entities.profiles.ProfileEstimation;

public interface LogicalToPhysicalOperationConverter {
    PhysicalOperation convert(ProfileEstimation profileEstimation) throws ColumnNotFoundException;
}
