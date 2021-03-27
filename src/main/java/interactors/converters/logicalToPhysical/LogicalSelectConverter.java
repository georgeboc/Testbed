package interactors.converters.logicalToPhysical;

import boundary.readers.ColumnReader;
import entities.exceptions.ColumnNotFoundException;
import entities.operations.logical.LogicalSelect;
import entities.operations.physical.PhysicalOperation;
import entities.operations.physical.PhysicalSelect;
import entities.profiles.ColumnProfile;
import entities.profiles.Profile;
import entities.profiles.ProfileEstimation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LogicalSelectConverter implements LogicalOperationConverter {
    private final ColumnReader columnReader;

    @Override
    public PhysicalOperation convert(ProfileEstimation profileEstimation) {
        Profile profile = profileEstimation.getProfile();
        LogicalSelect logicalSelect = (LogicalSelect) profileEstimation.getLogicalOperation();
        ColumnProfile columnProfile = profile.getColumns().get(logicalSelect.getColumnName());
        if (columnProfile == null) {
            throw new ColumnNotFoundException(logicalSelect.getColumnName());
        }
        long rowId = (long) ((double) columnProfile.getRowsCount() * logicalSelect.getSelectivityFactor());
        String value = columnReader.getValueFromRowId(rowId, logicalSelect.getColumnName(), profileEstimation.getColumnStatsPath());
        return PhysicalSelect.builder()
                .columnName(logicalSelect.getColumnName())
                .lessThanValue(value)
                .build();
    }
}
