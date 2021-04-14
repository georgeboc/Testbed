package com.testbed.interactors.converters.logicalToPhysical;

import com.testbed.entities.exceptions.ColumnNotFoundException;
import com.testbed.entities.exceptions.TolerableErrorPercentageExceeded;
import com.testbed.entities.operations.logical.LogicalProject;
import com.testbed.entities.operations.physical.PhysicalOperation;
import com.testbed.entities.operations.physical.PhysicalProject;
import com.testbed.entities.profiles.ProfileEstimation;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.Math.abs;

public class ProjectLogicalToPhysicalConverter implements LogicalToPhysicalConverter {
    @Override
    public PhysicalOperation convert(final ProfileEstimation profileEstimation) throws ColumnNotFoundException {
        Set<String> originalColumnNames = profileEstimation.getProfile().getColumns().keySet();
        LogicalProject logicalProject = (LogicalProject) profileEstimation.getLogicalOperation();
        long approximatedOutputColumnsCount = (long) (logicalProject.getColumnsSelectionFactor() * originalColumnNames.size());
        List<String> projectedColumnNames = originalColumnNames.stream()
                .sorted()
                .limit(approximatedOutputColumnsCount)
                .collect(Collectors.toList());
        checkIfErrorIsTolerable(originalColumnNames.size(),
                projectedColumnNames.size(),
                logicalProject.getColumnsSelectionFactor(),
                profileEstimation.getTolerableErrorPercentage());
        return PhysicalProject.builder()
                .id(logicalProject.getId())
                .projectedColumnNames(projectedColumnNames)
                .approximatedColumnsSelectionFactor(logicalProject.getColumnsSelectionFactor())
                .build();
    }

    private void checkIfErrorIsTolerable(final long inputColumnsCount,
                                         final long outputColumnsCount,
                                         final double approximatedColumnsSelectionFactor,
                                         final double tolerableErrorPercentage) {
        double errorPercentage = abs((double)outputColumnsCount/inputColumnsCount - approximatedColumnsSelectionFactor)*100;
        if (errorPercentage > tolerableErrorPercentage) {
            throw new TolerableErrorPercentageExceeded(errorPercentage, tolerableErrorPercentage);
        }
    }
}
