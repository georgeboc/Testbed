package com.testbed.interactors.converters.logicalToPhysical;

import com.testbed.entities.exceptions.ColumnNotFoundException;
import com.testbed.entities.operations.logical.LogicalProject;
import com.testbed.entities.operations.physical.PhysicalOperation;
import com.testbed.entities.operations.physical.PhysicalProject;
import com.testbed.entities.profiles.ProfileEstimation;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class LogicalToPhysicalProjectConverter implements LogicalToPhysicalOperationConverter {
    @Override
    public PhysicalOperation convert(ProfileEstimation profileEstimation) throws ColumnNotFoundException {
        Set<String> columnNames = profileEstimation.getProfile().getColumns().keySet();
        LogicalProject logicalProject = (LogicalProject) profileEstimation.getLogicalOperation();
        long expectedOutputColumnsCount = (long) (logicalProject.getColumnsSelectionFactor() * columnNames.size());
        List<String> projectedColumnNames = columnNames.stream()
                .limit(expectedOutputColumnsCount)
                .collect(Collectors.toList());
        return new PhysicalProject(projectedColumnNames, expectedOutputColumnsCount);
    }
}
