package com.testbed.interactors.converters.logicalToPhysical;

import com.testbed.entities.exceptions.ColumnNotFoundException;
import com.testbed.entities.operations.logical.LogicalJoin;
import com.testbed.entities.operations.physical.PhysicalJoin;
import com.testbed.entities.operations.physical.PhysicalOperation;
import com.testbed.entities.profiles.ProfileEstimation;

public class JoinLogicalToPhysicalConverter implements LogicalToPhysicalConverter {
    @Override
    public PhysicalOperation convert(final ProfileEstimation profileEstimation) throws ColumnNotFoundException {
        LogicalJoin logicalJoin = (LogicalJoin) profileEstimation.getLogicalOperation();
        return PhysicalJoin.builder()
                .id(logicalJoin.getId())
                .joinColumnName(logicalJoin.getJoinColumnName())
                .build();
    }
}
