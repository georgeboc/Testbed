package com.testbed.interactors.converters.logicalToPhysical;

import com.testbed.entities.exceptions.ColumnNotFoundException;
import com.testbed.entities.operations.logical.LogicalProject;
import com.testbed.entities.operations.physical.PhysicalOperation;
import com.testbed.entities.operations.physical.PhysicalProject;
import com.testbed.entities.profiles.ProfileEstimation;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.testbed.interactors.converters.ConvertersCommons.checkIfErrorIsTolerable;

public class ProjectLogicalToPhysicalConverter implements LogicalToPhysicalConverter {
    @Override
    public PhysicalOperation convert(final ProfileEstimation profileEstimation) throws ColumnNotFoundException {
        Set<String> originalColumnNames = profileEstimation.getProfile().getColumns().keySet();
        LogicalProject logicalProject = (LogicalProject) profileEstimation.getLogicalOperation();
        long realOutputColumnsCount = (long) (logicalProject.getApproximatedColumnSelectivityFactor() * originalColumnNames.size());
        List<String> projectedColumnNames = originalColumnNames.stream()
                .sorted()
                .limit(realOutputColumnsCount)
                .collect(Collectors.toList());
        double realColumnSelectivityFactor = (double) projectedColumnNames.size()/originalColumnNames.size();
        checkIfErrorIsTolerable(realColumnSelectivityFactor,
                logicalProject.getApproximatedColumnSelectivityFactor(),
                profileEstimation.getTolerableErrorPercentage());
        return PhysicalProject.builder()
                .id(logicalProject.getId())
                .projectedColumnNames(projectedColumnNames)
                .approximatedColumnSelectivityFactor(logicalProject.getApproximatedColumnSelectivityFactor())
                .build();
    }
}
