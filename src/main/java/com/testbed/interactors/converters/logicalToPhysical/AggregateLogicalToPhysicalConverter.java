package com.testbed.interactors.converters.logicalToPhysical;

import com.testbed.entities.operations.logical.LogicalAggregate;
import com.testbed.entities.operations.physical.PhysicalAggregate;
import com.testbed.entities.operations.physical.PhysicalOperation;
import com.testbed.entities.profiles.ProfileEstimation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AggregateLogicalToPhysicalConverter implements LogicalToPhysicalConverter {
    @Override
    public PhysicalOperation convert(final ProfileEstimation profileEstimation) {
        LogicalAggregate logicalAggregate = (LogicalAggregate) profileEstimation.getLogicalOperation();
        return PhysicalAggregate.builder()
                ._id(logicalAggregate.get_id())
                .aggregationColumnName(logicalAggregate.getAggregationColumnName())
                .aggregationOperation(logicalAggregate.getAggregationOperation())
                .build();
    }
}
