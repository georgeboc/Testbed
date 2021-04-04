package com.testbed.interactors.converters.logicalToPhysical;

import com.testbed.entities.exceptions.ColumnNotFoundException;
import com.testbed.entities.operations.logical.LogicalUnion;
import com.testbed.entities.operations.physical.PhysicalOperation;
import com.testbed.entities.operations.physical.PhysicalUnion;
import com.testbed.entities.profiles.ProfileEstimation;

public class LogicalToPhysicalUnionConverter implements LogicalToPhysicalOperationConverter {
    @Override
    public PhysicalOperation convert(ProfileEstimation profileEstimation) throws ColumnNotFoundException {
        LogicalUnion logicalUnion = (LogicalUnion) profileEstimation.getLogicalOperation();
        return new PhysicalUnion(logicalUnion.get_id());
    }
}
