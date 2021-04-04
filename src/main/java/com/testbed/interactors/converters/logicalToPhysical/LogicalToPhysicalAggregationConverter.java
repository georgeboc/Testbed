package com.testbed.interactors.converters.logicalToPhysical;

import com.testbed.entities.operations.logical.LogicalAggregation;
import com.testbed.entities.operations.physical.PhysicalAggregation;
import com.testbed.entities.operations.physical.PhysicalOperation;
import com.testbed.entities.profiles.ProfileEstimation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LogicalToPhysicalAggregationConverter implements LogicalToPhysicalOperationConverter {
    @Override
    public PhysicalOperation convert(final ProfileEstimation profileEstimation) {
        LogicalAggregation logicalAggregation = (LogicalAggregation) profileEstimation.getLogicalOperation();
        return PhysicalAggregation.builder()
                ._id(logicalAggregation.get_id())
                .aggregationColumnName(logicalAggregation.getAggregationColumnName())
                .operation(logicalAggregation.getOperation())
                .build();
    }
}
