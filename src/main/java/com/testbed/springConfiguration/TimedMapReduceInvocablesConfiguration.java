package com.testbed.springConfiguration;

import com.jcraft.jsch.MAC;
import com.testbed.boundary.invocations.Invokable;
import com.testbed.boundary.invocations.frameworks.mapReduce.groupBy.GroupByMapReduceOperation;
import com.testbed.boundary.invocations.frameworks.mapReduce.JobConfigurationCommons;
import com.testbed.boundary.invocations.frameworks.mapReduce.join.JoinMapReduceOperation;
import com.testbed.boundary.invocations.frameworks.mapReduce.load.LoadMapReduceOperation;
import com.testbed.boundary.invocations.frameworks.mapReduce.project.ProjectMapReduceOperation;
import com.testbed.boundary.invocations.frameworks.mapReduce.select.SelectMapReduceOperation;
import com.testbed.boundary.invocations.frameworks.mapReduce.sink.SinkMapReduceOperation;
import com.testbed.boundary.invocations.frameworks.mapReduce.sumAggregator.SumAggregateMapReduceOperation;
import com.testbed.boundary.invocations.frameworks.mapReduce.union.UnionMapReduceOperation;
import com.testbed.boundary.metrics.MetricsQuery;
import com.testbed.boundary.utils.DirectoryUtils;
import com.testbed.boundary.utils.ParquetSchemaReader;
import com.testbed.interactors.monitors.AverageMemoryUtilizationMonitor;
import com.testbed.interactors.monitors.AverageSwapUtilizationMonitor;
import com.testbed.interactors.monitors.CPUIoWaitTimeMonitor;
import com.testbed.interactors.monitors.CPUSystemTimeMonitor;
import com.testbed.interactors.monitors.CPUTotalTimeMonitor;
import com.testbed.interactors.monitors.CPUUserTimeMonitor;
import com.testbed.interactors.monitors.ChronometerMonitor;
import com.testbed.interactors.monitors.DistributedFileSystemMonitor;
import com.testbed.interactors.monitors.ExecutionInstantsMonitor;
import com.testbed.interactors.monitors.InstantMetricsDifferencesCalculator;
import com.testbed.interactors.monitors.LocalFileSystemReadBytesMonitor;
import com.testbed.interactors.monitors.LocalFileSystemWrittenBytesMonitor;
import com.testbed.interactors.monitors.MaxMemoryUtilizationMonitor;
import com.testbed.interactors.monitors.MaxSwapUtilizationMonitor;
import com.testbed.interactors.monitors.MinMemoryUtilizationMonitor;
import com.testbed.interactors.monitors.MinSwapUtilizationMonitor;
import com.testbed.interactors.monitors.MonitorComposer;
import com.testbed.interactors.monitors.NetworkReceivedBytesMonitor;
import com.testbed.interactors.monitors.NetworkTransmittedBytesMonitor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;

import static com.testbed.springConfiguration.FrameworksConfigurationsConstants.TIMED_MAPREDUCE;
import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_AGGREGATE;
import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_GROUP_BY;
import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_JOIN;
import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_LOAD;
import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_PROJECT;
import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_SELECT;
import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_SINK;
import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_UNION;


@Configuration
@Profile(TIMED_MAPREDUCE)
public class TimedMapReduceInvocablesConfiguration {
    @Bean
    @Qualifier(PHYSICAL_LOAD)
    public Invokable mapReduceLoadInvokable() {
        return new LoadMapReduceOperation();
    }

    @Bean
    @Qualifier(PHYSICAL_SELECT)
    public Invokable mapReduceSelectInvokable(JobConfigurationCommons jobConfigurationCommons,
                                              ParquetSchemaReader parquetSchemaReader) {
        return new SelectMapReduceOperation(jobConfigurationCommons, parquetSchemaReader);
    }

    @Bean
    @Qualifier(PHYSICAL_PROJECT)
    public Invokable mapReduceProjectInvokable(JobConfigurationCommons jobConfigurationCommons,
                                               ParquetSchemaReader parquetSchemaReader) {
        return new ProjectMapReduceOperation(jobConfigurationCommons, parquetSchemaReader);
    }

    @Bean
    @Qualifier(PHYSICAL_GROUP_BY)
    public Invokable mapReduceGroupByInvokable(JobConfigurationCommons jobConfigurationCommons,
                                               ParquetSchemaReader parquetSchemaReader) {
        return new GroupByMapReduceOperation(jobConfigurationCommons, parquetSchemaReader);
    }

    @Bean
    @Qualifier(PHYSICAL_AGGREGATE)
    public Invokable mapReduceAggregateInvokable(JobConfigurationCommons jobConfigurationCommons) {
        return new SumAggregateMapReduceOperation(jobConfigurationCommons);
    }

    @Bean
    @Qualifier(PHYSICAL_JOIN)
    public Invokable mapReduceJoinInvokable(JobConfigurationCommons jobConfigurationCommons,
                                            ParquetSchemaReader parquetSchemaReader) {
        return new JoinMapReduceOperation(jobConfigurationCommons, parquetSchemaReader);
    }

    @Bean
    @Qualifier(PHYSICAL_UNION)
    public Invokable mapReduceUnionInvokable(JobConfigurationCommons jobConfigurationCommons,
                                             ParquetSchemaReader parquetSchemaReader) {
        return new UnionMapReduceOperation(jobConfigurationCommons, parquetSchemaReader);
    }

    @Bean
    @Qualifier(PHYSICAL_SINK)
    public Invokable mapReduceSinkInvokable() {
        return new SinkMapReduceOperation();
    }

    @Bean
    public ParquetSchemaReader parquetSchemaReader(org.apache.hadoop.conf.Configuration configuration,
                                                   DirectoryUtils directoryUtils) {
        return new ParquetSchemaReader(configuration, directoryUtils);
    }

    @Bean
    public JobConfigurationCommons jobConfigurationCommons(org.apache.hadoop.conf.Configuration configuration) {
        return new JobConfigurationCommons(configuration);
    }

    @Bean
    public LocalFileSystemWrittenBytesMonitor localFileSystemWrittenBytesMonitor(@Value("${localFileSystemDevice.mapReduce}") String deviceName,
                                                                                 InstantMetricsDifferencesCalculator instantMetricsDifferencesCalculator) {
        return new LocalFileSystemWrittenBytesMonitor(instantMetricsDifferencesCalculator, deviceName);
    }

    @Bean
    public LocalFileSystemReadBytesMonitor localFileSystemReadBytesMonitor(@Value("${localFileSystemDevice.mapReduce}") String deviceName,
                                                                              InstantMetricsDifferencesCalculator instantMetricsDifferencesCalculator) {
        return new LocalFileSystemReadBytesMonitor(instantMetricsDifferencesCalculator, deviceName);
    }

    @Bean
    public MonitorComposer monitorComposer(ChronometerMonitor chronometerMonitor,
                                           ExecutionInstantsMonitor executionInstantsMonitor,
                                           DistributedFileSystemMonitor distributedFileSystemMonitor,
                                           LocalFileSystemWrittenBytesMonitor localFileSystemWrittenBytesMonitor,
                                           LocalFileSystemReadBytesMonitor localFileSystemReadBytesMonitor,
                                           CPUIoWaitTimeMonitor cpuIoWaitTimeMonitor,
                                           CPUSystemTimeMonitor cpuSystemTimeMonitor,
                                           CPUTotalTimeMonitor cpuTotalTimeMonitor,
                                           CPUUserTimeMonitor cpuUserTimeMonitor,
                                           MinMemoryUtilizationMonitor minMemoryUtilizationMonitor,
                                           MaxMemoryUtilizationMonitor maxMemoryUtilizationMonitor,
                                           AverageMemoryUtilizationMonitor averageMemoryUtilizationMonitor,
                                           MinSwapUtilizationMonitor minSwapUtilizationMonitor,
                                           MaxSwapUtilizationMonitor maxSwapUtilizationMonitor,
                                           AverageSwapUtilizationMonitor averageSwapUtilizationMonitor,
                                           NetworkReceivedBytesMonitor networkReceivedBytesMonitor,
                                           NetworkTransmittedBytesMonitor networkTransmittedBytesMonitor) {
        // Leftmost monitor is the one that will get executed first. In this case, it is fundamental that the
        // chronometer gets executed first to avoid to interfere in execution time.
        return new MonitorComposer(Arrays.asList(chronometerMonitor,
                executionInstantsMonitor,
                distributedFileSystemMonitor,
                localFileSystemWrittenBytesMonitor,
                localFileSystemReadBytesMonitor,
                cpuIoWaitTimeMonitor,
                cpuSystemTimeMonitor,
                cpuTotalTimeMonitor,
                cpuUserTimeMonitor,
                minMemoryUtilizationMonitor,
                maxMemoryUtilizationMonitor,
                averageMemoryUtilizationMonitor,
                minSwapUtilizationMonitor,
                maxSwapUtilizationMonitor,
                averageSwapUtilizationMonitor,
                networkReceivedBytesMonitor,
                networkTransmittedBytesMonitor));
    }
}
