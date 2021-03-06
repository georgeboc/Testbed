package com.testbed.springConfiguration;

import com.clearspring.analytics.util.Lists;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.testbed.boundary.deserializers.AvroProfileDeserializer;
import com.testbed.boundary.deserializers.DeserializedOperationMixin;
import com.testbed.boundary.deserializers.Deserializer;
import com.testbed.boundary.deserializers.JsonOperationsDeserializer;
import com.testbed.boundary.invocations.instrumentation.OperationInstrumentation;
import com.testbed.boundary.metrics.MetricsQuery;
import com.testbed.boundary.metrics.prometheus.PrometheusMetricsQuery;
import com.testbed.boundary.readers.AvroColumnReader;
import com.testbed.boundary.readers.ColumnReader;
import com.testbed.boundary.utils.DirectoryUtils;
import com.testbed.boundary.writers.SpreadsheetWriter;
import com.testbed.boundary.writers.XLSXSpreadsheetWriter;
import com.testbed.entities.operations.deserialized.DeserializedOperation;
import com.testbed.entities.operations.deserialized.DeserializedOperations;
import com.testbed.entities.profiles.Profile;
import com.testbed.interactors.InstrumentedInvocationsInteractor;
import com.testbed.interactors.Interactor;
import com.testbed.interactors.InteractorCommons;
import com.testbed.interactors.TimedInvocationsInteractor;
import com.testbed.interactors.converters.deserializedToLogical.DeserializedToLogicalConverterManager;
import com.testbed.interactors.converters.logicalToPhysical.LogicalToPhysicalConverterManager;
import com.testbed.interactors.invokers.InvocationPlanner;
import com.testbed.interactors.invokers.InvokerManager;
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
import com.testbed.interactors.monitors.NoMonitor;
import com.testbed.interactors.monitors.RangeMetricsAggregateCalculator;
import com.testbed.interactors.validators.semantic.InputsCountValidatorManager;
import com.testbed.interactors.validators.syntactic.NotNullOnAllFieldsValidatorManager;
import com.testbed.interactors.viewers.InstrumentatedInvocationsViewer;
import com.testbed.interactors.viewers.MonitoringInformationViewer;
import com.testbed.interactors.viewers.TimedInvocationsViewer;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocalFileSystem;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static com.testbed.interactors.InteractorName.INSTRUMENTED;
import static com.testbed.interactors.InteractorName.TIMED;

@Configuration
@PropertySources({
        @PropertySource("classpath:application.properties"),
        @PropertySource("classpath:${environment_properties_filename}")
})
public class ApplicationConfiguration {
    private static final String OBJECT_MAPPER_WITH_DESERIALIZED_OPERATION_MIXIN = "objectMapperWithDeserializedOperationMixin";
    private static final String OBJECT_MAPPER = "objectMapper";
    private static final boolean DISABLED = false;

    @Bean
    public InteractorCommons interactorCommons(Deserializer<DeserializedOperations> operationsDeserializer,
                                               NotNullOnAllFieldsValidatorManager notNullOnAllFieldsValidatorManager,
                                               DeserializedToLogicalConverterManager deserializedToLogicalConverterManager,
                                               InputsCountValidatorManager inputsCountValidatorManager,
                                               LogicalToPhysicalConverterManager logicalToPhysicalConverterManager,
                                               InvocationPlanner invocationPlanner) {
        return new InteractorCommons(operationsDeserializer,
                notNullOnAllFieldsValidatorManager,
                deserializedToLogicalConverterManager,
                inputsCountValidatorManager,
                logicalToPhysicalConverterManager,
                invocationPlanner);
    }

    @Bean
    @Qualifier(INSTRUMENTED)
    public Interactor instrumentedInvocationsInteractor(InteractorCommons interactorCommons,
                                                        InvokerManager invokerManager,
                                                        List<OperationInstrumentation> operationInstrumentations,
                                                        InstrumentatedInvocationsViewer instrumentatedInvocationsViewer,
                                                        @Qualifier(OBJECT_MAPPER) ObjectMapper objectMapper) {
        return new InstrumentedInvocationsInteractor(interactorCommons,
                invokerManager,
                operationInstrumentations,
                instrumentatedInvocationsViewer,
                objectMapper);
    }

    @Bean
    @Qualifier(TIMED)
    public Interactor timedInvocationsInteractor(InteractorCommons interactorCommons,
                                                 InvokerManager invokerManager,
                                                 TimedInvocationsViewer timedInvocationsViewer,
                                                 @Qualifier(OBJECT_MAPPER) ObjectMapper objectMapper) {
        return new TimedInvocationsInteractor(interactorCommons, invokerManager, timedInvocationsViewer, objectMapper);
    }

    @Bean
    public Deserializer<DeserializedOperations> operationsDeserializer(FileSystem fileSystem,
            @Qualifier(OBJECT_MAPPER_WITH_DESERIALIZED_OPERATION_MIXIN) ObjectMapper objectMapperWithDeserializedOperationMixin) {
        return new JsonOperationsDeserializer(fileSystem, objectMapperWithDeserializedOperationMixin);
    }

    @Bean
    public FileSystem fileSystem(@Value("${clusterMode.filesystemURI}") String filesystemURI,
                                 org.apache.hadoop.conf.Configuration configuration) throws IOException, URISyntaxException {
        return FileSystem.get(new URI(filesystemURI), configuration);
    }

    @Bean
    public org.apache.hadoop.conf.Configuration configuration(@Value("${clusterMode.mapReduce}") String mapReduceClusterMode) {
        org.apache.hadoop.conf.Configuration configuration = new org.apache.hadoop.conf.Configuration();
        configuration.set("mapreduce.framework.name", mapReduceClusterMode);
        configuration.set("fs.hdfs.impl", DistributedFileSystem.class.getName());
        configuration.set("fs.file.impl", LocalFileSystem.class.getName());
        return configuration;
    }

    @Bean
    @Qualifier(OBJECT_MAPPER_WITH_DESERIALIZED_OPERATION_MIXIN)
    public ObjectMapper objectMapperWithDeserializedOperationMixin() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.addMixIn(DeserializedOperation.class, DeserializedOperationMixin.class);
        return objectMapper;
    }

    @Bean
    public InvocationPlanner invocationPlanner() {
        return new InvocationPlanner();
    }

    @Bean
    public InvokerManager invokerManager(MonitorComposer monitorComposer) {
        return new InvokerManager(monitorComposer);
    }

    @Bean
    public List<OperationInstrumentation> operationInstrumentations() {
        return Lists.newArrayList();
    }

    @Bean
    public InstrumentatedInvocationsViewer invocationInstrumentationViewer(SpreadsheetWriter spreadsheetWriter,
                                                                           @Qualifier(OBJECT_MAPPER) ObjectMapper objectMapper) {
        return new InstrumentatedInvocationsViewer(spreadsheetWriter, objectMapper);
    }

    @Bean
    public TimedInvocationsViewer timedInvocationsViewer(SpreadsheetWriter spreadsheetWriter,
                                                         MonitoringInformationViewer monitoringInformationViewer) {
        return new TimedInvocationsViewer(spreadsheetWriter, monitoringInformationViewer);
    }

    @Bean
    @Qualifier(OBJECT_MAPPER)
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, DISABLED);
        return objectMapper;
    }

    @Bean
    public ColumnReader getColumnReader(org.apache.hadoop.conf.Configuration configuration,
                                        DirectoryUtils directoryUtils) {
        return new AvroColumnReader(configuration, directoryUtils);
    }

    @Bean
    public DirectoryUtils directoryUtils(FileSystem fileSystem) {
        return new DirectoryUtils(fileSystem);
    }

    @Bean
    public SpreadsheetWriter spreadsheetWriter(FileSystem fileSystem) {
        return new XLSXSpreadsheetWriter(fileSystem);
    }

    @Bean
    public MonitoringInformationViewer monitoringInformationViewer(SpreadsheetWriter spreadsheetWriter) {
        return new MonitoringInformationViewer(spreadsheetWriter);
    }

    @Bean
    public Deserializer<Profile> profileDeserializer(org.apache.hadoop.conf.Configuration configuration,
                                                     DirectoryUtils directoryUtils) {
        return new AvroProfileDeserializer(configuration, directoryUtils);
    }

    @Bean
    public MetricsQuery prometheusMetricsQuery(@Value("${prometheus.api.baseUrl}") String baseUrl) {
        return new PrometheusMetricsQuery(baseUrl);
    }

    @Bean
    public NoMonitor noMonitor() {
        return new NoMonitor();
    }

    @Bean
    public ChronometerMonitor chronometerMonitor() {
        return new ChronometerMonitor();
    }

    @Bean
    public ExecutionInstantsMonitor executionInstantsMonitor() {
        return new ExecutionInstantsMonitor();
    }

    @Bean
    public InstantMetricsDifferencesCalculator instantMetricsDifferencesCalculator(MetricsQuery metricsQuery) {
        return new InstantMetricsDifferencesCalculator(metricsQuery);
    }

    @Bean
    public CPUIoWaitTimeMonitor cpuIoWaitTimeMonitor(InstantMetricsDifferencesCalculator instantMetricsDifferencesCalculator) {
        return new CPUIoWaitTimeMonitor(instantMetricsDifferencesCalculator);
    }

    @Bean
    public CPUSystemTimeMonitor cpuSystemTimeMonitor(InstantMetricsDifferencesCalculator instantMetricsDifferencesCalculator) {
        return new CPUSystemTimeMonitor(instantMetricsDifferencesCalculator);
    }

    @Bean
    public CPUTotalTimeMonitor cpuTotalTimeMonitor(InstantMetricsDifferencesCalculator instantMetricsDifferencesCalculator) {
        return new CPUTotalTimeMonitor(instantMetricsDifferencesCalculator);
    }

    @Bean
    public CPUUserTimeMonitor cpuUserTimeMonitor(InstantMetricsDifferencesCalculator instantMetricsDifferencesCalculator) {
        return new CPUUserTimeMonitor(instantMetricsDifferencesCalculator);
    }

    @Bean
    public MinMemoryUtilizationMonitor minMemoryUtilizationMonitor(RangeMetricsAggregateCalculator rangeMetricsAggregateCalculator) {
        return new MinMemoryUtilizationMonitor(rangeMetricsAggregateCalculator);
    }

    @Bean
    public MaxMemoryUtilizationMonitor maxMemoryUtilizationMonitor(RangeMetricsAggregateCalculator rangeMetricsAggregateCalculator) {
        return new MaxMemoryUtilizationMonitor(rangeMetricsAggregateCalculator);
    }

    @Bean
    public AverageMemoryUtilizationMonitor averageMemoryUtilizationMonitor(RangeMetricsAggregateCalculator rangeMetricsAggregateCalculator) {
        return new AverageMemoryUtilizationMonitor(rangeMetricsAggregateCalculator);
    }

    @Bean
    public MinSwapUtilizationMonitor minSwapUtilizationMonitor(RangeMetricsAggregateCalculator rangeMetricsAggregateCalculator) {
        return new MinSwapUtilizationMonitor(rangeMetricsAggregateCalculator);
    }

    @Bean
    public MaxSwapUtilizationMonitor maxSwapUtilizationMonitor(RangeMetricsAggregateCalculator rangeMetricsAggregateCalculator) {
        return new MaxSwapUtilizationMonitor(rangeMetricsAggregateCalculator);
    }

    @Bean
    public AverageSwapUtilizationMonitor averageSwapUtilizationMonitor(RangeMetricsAggregateCalculator rangeMetricsAggregateCalculator) {
        return new AverageSwapUtilizationMonitor(rangeMetricsAggregateCalculator);
    }

    @Bean
    public NetworkReceivedBytesMonitor networkReceivedBytesMonitor(InstantMetricsDifferencesCalculator instantMetricsDifferencesCalculator) {
        return new NetworkReceivedBytesMonitor(instantMetricsDifferencesCalculator);
    }

    @Bean
    public NetworkTransmittedBytesMonitor networkTransmittedBytesMonitor(InstantMetricsDifferencesCalculator instantMetricsDifferencesCalculator) {
        return new NetworkTransmittedBytesMonitor(instantMetricsDifferencesCalculator);
    }

    @Bean
    public LocalFileSystemWrittenBytesMonitor localFileSystemWrittenBytesMonitor(@Value("${yarnLocalDevice}") String deviceName,
                                                                                 InstantMetricsDifferencesCalculator instantMetricsDifferencesCalculator) {
        return new LocalFileSystemWrittenBytesMonitor(instantMetricsDifferencesCalculator, deviceName);
    }

    @Bean
    public LocalFileSystemReadBytesMonitor localFileSystemReadBytesMonitor(@Value("${yarnLocalDevice}") String deviceName,
                                                                           InstantMetricsDifferencesCalculator instantMetricsDifferencesCalculator) {
        return new LocalFileSystemReadBytesMonitor(instantMetricsDifferencesCalculator, deviceName);
    }

    @Bean
    public RangeMetricsAggregateCalculator rangeMetricsAggregateCalculator(MetricsQuery metricsQuery) {
        return new RangeMetricsAggregateCalculator(metricsQuery);
    }

    @Bean
    public DistributedFileSystemMonitor distributedFileSystemMonitor(FileSystem fileSystem) {
        return new DistributedFileSystemMonitor(fileSystem);
    }
}
