package com.testbed.interactors.monitors;

import com.testbed.entities.invocations.InvocationPlan;
import lombok.RequiredArgsConstructor;
import org.apache.hadoop.fs.ContentSummary;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class DistributedFileSystemMonitor implements Monitor {
    private static final String INTERMEDIATE_DATASETS_DIRECTORY_PREFIX = "intermediate_datasets/";
    private static final boolean RECURSIVELY = true;
    private static final String DISTRIBUTED_FILESYSTEM_SIZE_WITH_REPLICATION = "distributedFileSystemSizeWithReplication";
    private static final String DISTRIBUTED_FILESYSTEM_SIZE_WITHOUT_REPLICATION = "distributedFileSystemSizeWithoutReplication";

    private final FileSystem fileSystem;
    private final MonitoringInformationCoalesce monitoringInformationCoalesce;

    @Override
    public MonitoringInformation monitor(Callable<MonitoringInformation> callable,
                                         InvocationPlan invocationPlan) {
        tryDeleteDirectory(INTERMEDIATE_DATASETS_DIRECTORY_PREFIX);
        MonitoringInformation callableMonitoringInformation = MonitorCommons.tryCall(callable);
        List<String> directoriesToExclude = getDirectoriesToExclude(invocationPlan);
        directoriesToExclude.forEach(this::tryDeleteDirectory);
        return monitoringInformationCoalesce.coalesce(callableMonitoringInformation, getMonitoringInformation());
    }

    private void tryDeleteDirectory(String directory) {
        try {
            fileSystem.delete(new Path(directory), RECURSIVELY);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private List<String> getDirectoriesToExclude(InvocationPlan invocationPlan) {
        return invocationPlan.getOperationInvocations().stream()
                .filter(operationInvocation -> operationInvocation.isLastOperationBeforeSink() ||
                        operationInvocation.getSucceedingPhysicalOperationsCount() == 0)
                .map(operationInvocation -> INTERMEDIATE_DATASETS_DIRECTORY_PREFIX + operationInvocation.getPhysicalOperation().getId())
                .collect(Collectors.toList());
    }

    private MonitoringInformation getMonitoringInformation() {
        MonitoringInformation monitoringInformation = MonitoringInformation.createNew();
        ContentSummary contentSummary = tryGetContentSummary();
        monitoringInformation.getResult().put(DISTRIBUTED_FILESYSTEM_SIZE_WITH_REPLICATION,
                String.valueOf(contentSummary.getSpaceConsumed()));
        monitoringInformation.getResult().put(DISTRIBUTED_FILESYSTEM_SIZE_WITHOUT_REPLICATION,
                String.valueOf(contentSummary.getLength()));
        return monitoringInformation;
    }

    private ContentSummary tryGetContentSummary() {
        try {
            return fileSystem.getContentSummary(new Path(INTERMEDIATE_DATASETS_DIRECTORY_PREFIX));
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
