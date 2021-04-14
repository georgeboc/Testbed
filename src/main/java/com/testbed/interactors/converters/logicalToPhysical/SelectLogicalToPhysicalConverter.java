package com.testbed.interactors.converters.logicalToPhysical;

import com.testbed.boundary.readers.ColumnReader;
import com.testbed.entities.exceptions.ColumnNotFoundException;
import com.testbed.entities.operations.logical.LogicalSelect;
import com.testbed.entities.operations.physical.PhysicalOperation;
import com.testbed.entities.operations.physical.PhysicalSelect;
import com.testbed.entities.profiles.ColumnProfile;
import com.testbed.entities.profiles.Profile;
import com.testbed.entities.profiles.ProfileEstimation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SelectLogicalToPhysicalConverter implements LogicalToPhysicalConverter {
    private final ColumnReader columnReader;

    @Override
    public PhysicalOperation convert(final ProfileEstimation profileEstimation) {
        Profile profile = profileEstimation.getProfile();
        LogicalSelect logicalSelect = (LogicalSelect) profileEstimation.getLogicalOperation();
        if (!profile.getColumns().containsKey(logicalSelect.getColumnName())) {
            throw new ColumnNotFoundException(logicalSelect.getColumnName());
        }
        ColumnProfile columnProfile = profile.getColumns().get(logicalSelect.getColumnName());
        String value = columnReader.getValueFromSelectivityFactor(logicalSelect.getSelectivityFactor(),
                columnProfile.getDistinctRowsCount(),
                logicalSelect.getColumnName(),
                profileEstimation.getColumnStatsPath());
        long approximatedOutputRowsCount = (long) (columnProfile.getTotalRowsCount()*logicalSelect.getSelectivityFactor());
        return PhysicalSelect.builder()
                .id(logicalSelect.getId())
                .columnName(logicalSelect.getColumnName())
                .lessThanOrEqualValue(value)
                .approximatedOutputRowsCount(approximatedOutputRowsCount)
                .build();
    }
}
