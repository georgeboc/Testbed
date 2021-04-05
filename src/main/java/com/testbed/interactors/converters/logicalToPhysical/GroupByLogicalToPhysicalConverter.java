package com.testbed.interactors.converters.logicalToPhysical;

import com.testbed.entities.operations.logical.LogicalGroupBy;
import com.testbed.entities.operations.physical.PhysicalGroupBy;
import com.testbed.entities.operations.physical.PhysicalOperation;
import com.testbed.entities.profiles.ProfileEstimation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GroupByLogicalToPhysicalConverter implements LogicalToPhysicalConverter {
    @Override
    public PhysicalOperation convert(final ProfileEstimation profileEstimation) {
        LogicalGroupBy logicalOperation = (LogicalGroupBy) profileEstimation.getLogicalOperation();
        return new PhysicalGroupBy(logicalOperation.getId());
    }
}
