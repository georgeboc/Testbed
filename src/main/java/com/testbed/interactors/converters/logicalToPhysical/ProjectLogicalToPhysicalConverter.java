package com.testbed.interactors.converters.logicalToPhysical;

import com.testbed.entities.exceptions.ColumnNotFoundException;
import com.testbed.entities.operations.logical.LogicalProject;
import com.testbed.entities.operations.physical.PhysicalOperation;
import com.testbed.entities.operations.physical.PhysicalProject;
import com.testbed.entities.profiles.ProfileEstimation;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ProjectLogicalToPhysicalConverter implements LogicalToPhysicalConverter {
    @Override
    public PhysicalOperation convert(final ProfileEstimation profileEstimation) throws ColumnNotFoundException {
        Set<String> columnNames = profileEstimation.getProfile().getColumns().keySet();
        LogicalProject logicalProject = (LogicalProject) profileEstimation.getLogicalOperation();
        long expectedOutputColumnsCount = (long) (logicalProject.getColumnsSelectionFactor() * columnNames.size());
        List<String> projectedColumnNames = columnNames.stream()
                .sorted()
                .limit(expectedOutputColumnsCount)
                .collect(Collectors.toList());
        return PhysicalProject.builder()
                .id(logicalProject.getId())
                .projectedColumnNames(projectedColumnNames)
                .expectedColumnsSelectionFactor(logicalProject.getColumnsSelectionFactor())
                .build();
    }
}
