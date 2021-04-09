package com.testbed.interactors.converters.logicalToPhysical;

import com.testbed.entities.operations.logical.LogicalGroupby;
import com.testbed.entities.operations.physical.PhysicalGroupby;
import com.testbed.entities.operations.physical.PhysicalOperation;
import com.testbed.entities.profiles.ProfileEstimation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GroupByLogicalToPhysicalConverter implements LogicalToPhysicalConverter {
    @Override
    public PhysicalOperation convert(final ProfileEstimation profileEstimation) {
        LogicalGroupby logicalOperation = (LogicalGroupby) profileEstimation.getLogicalOperation();
        return new PhysicalGroupby(logicalOperation.getId());
    }
}
