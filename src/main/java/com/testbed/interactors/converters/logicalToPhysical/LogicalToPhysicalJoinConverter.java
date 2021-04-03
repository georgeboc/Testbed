package com.testbed.interactors.converters.logicalToPhysical;

import com.testbed.entities.exceptions.ColumnNotFoundException;
import com.testbed.entities.operations.logical.LogicalJoin;
import com.testbed.entities.operations.physical.PhysicalJoin;
import com.testbed.entities.operations.physical.PhysicalOperation;
import com.testbed.entities.profiles.ProfileEstimation;

public class LogicalToPhysicalJoinConverter implements LogicalToPhysicalOperationConverter {
    @Override
    public PhysicalOperation convert(ProfileEstimation profileEstimation) throws ColumnNotFoundException {
        LogicalJoin logicalJoin = (LogicalJoin) profileEstimation.getLogicalOperation();
        return PhysicalJoin.builder()
                ._id(logicalJoin.get_id())
                .joinColumnName(logicalJoin.getJoinColumnName())
                .build();
    }
}
