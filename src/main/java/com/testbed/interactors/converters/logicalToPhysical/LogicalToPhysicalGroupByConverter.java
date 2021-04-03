package com.testbed.interactors.converters.logicalToPhysical;

import com.testbed.entities.operations.physical.PhysicalOperation;
import com.testbed.entities.profiles.ProfileEstimation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LogicalToPhysicalGroupByConverter implements LogicalToPhysicalOperationConverter {
    @Override
    public PhysicalOperation convert(final ProfileEstimation profileEstimation) {
        return null;
    }
}
