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

import static com.testbed.interactors.converters.ConvertersCommons.checkIfErrorIsTolerable;
import static java.lang.Math.abs;

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
        String value = columnReader.getValueFromSelectivityFactor(logicalSelect.getApproximatedRowsSelectivityFactor(),
                columnProfile.getDistinctRowsCount(),
                logicalSelect.getColumnName(),
                profileEstimation.getColumnStatsPath());
        long realOutputRowsCount = (long) (columnProfile.getTotalRowsCount()*logicalSelect.getApproximatedRowsSelectivityFactor());
        double realSelectivityFactor = (double) realOutputRowsCount/columnProfile.getTotalRowsCount();
        checkIfErrorIsTolerable(realSelectivityFactor,
                logicalSelect.getApproximatedRowsSelectivityFactor(),
                profileEstimation.getTolerableErrorPercentage());
        return PhysicalSelect.builder()
                .id(logicalSelect.getId())
                .columnName(logicalSelect.getColumnName())
                .lessThanOrEqualValue(value)
                .build();
    }
}
