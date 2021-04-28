package com.testbed.springConfiguration;

import com.clearspring.analytics.util.Lists;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.testbed.boundary.deserializers.AvroProfileDeserializer;
import com.testbed.boundary.deserializers.DeserializedOperationMixin;
import com.testbed.boundary.deserializers.Deserializer;
import com.testbed.boundary.deserializers.JsonOperationsDeserializer;
import com.testbed.boundary.invocations.instrumentation.OperationInstrumentation;
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
import com.testbed.interactors.validators.semantic.InputsCountValidatorManager;
import com.testbed.interactors.validators.syntactic.NotNullOnAllFieldsValidatorManager;
import com.testbed.interactors.viewers.InstrumentatedInvocationsViewer;
import com.testbed.interactors.viewers.TimedInvocationsViewer;
import org.apache.hadoop.fs.FileSystem;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static com.testbed.interactors.InteractorName.INSTRUMENTED;
import static com.testbed.interactors.InteractorName.TIMED;

@Configuration
@PropertySource("classpath:${environment_properties_filename}")
public class ApplicationConfiguration {
    private static final String OBJECT_MAPPER_WITH_DESERIALIZED_OPERATION_MIXIN = "objectMapperWithDeserializedOperationMixin";
    private static final String OBJECT_MAPPER_WITH_JAVA_TIME_MODULE = "objectMapperWithJavaTimeModule";
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
                                                        ObjectMapper objectMapper) {
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
    public Deserializer<DeserializedOperations> operationsDeserializer(
            @Qualifier(OBJECT_MAPPER_WITH_DESERIALIZED_OPERATION_MIXIN) ObjectMapper objectMapperWithDeserializedOperationMixin) {
        return new JsonOperationsDeserializer(objectMapperWithDeserializedOperationMixin);
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
    public InvokerManager invokerManager() {
        return new InvokerManager();
    }

    @Bean
    public List<OperationInstrumentation> operationInstrumentations() {
        return Lists.newArrayList();
    }

    @Bean
    public InstrumentatedInvocationsViewer invocationInstrumentationViewer(SpreadsheetWriter spreadsheetWriter,
                                                                           @Qualifier(OBJECT_MAPPER_WITH_JAVA_TIME_MODULE) ObjectMapper objectMapper) {
        return new InstrumentatedInvocationsViewer(spreadsheetWriter, objectMapper);
    }

    @Bean
    public TimedInvocationsViewer timedInvocationsViewer(SpreadsheetWriter spreadsheetWriter) {
        return new TimedInvocationsViewer(spreadsheetWriter);
    }

    @Bean
    @Qualifier(OBJECT_MAPPER)
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, DISABLED);
        return objectMapper;
    }

    @Bean
    @Qualifier(OBJECT_MAPPER_WITH_JAVA_TIME_MODULE)
    public ObjectMapper objectMapperWithJavaTimeModule() {
        return JsonMapper.builder().build();
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
    public FileSystem fileSystem(@Value("${clusterMode.filesystemURI}") String filesystemURI,
                                 org.apache.hadoop.conf.Configuration configuration) throws IOException, URISyntaxException {
        return FileSystem.get(new URI(filesystemURI), configuration);
    }

    @Bean
    public org.apache.hadoop.conf.Configuration configuration(@Value("${clusterMode.mapReduce}") String mapReduceClusterMode) {
        org.apache.hadoop.conf.Configuration configuration = new org.apache.hadoop.conf.Configuration();
        configuration.set("mapreduce.framework.name", mapReduceClusterMode);
        return configuration;
    }

    @Bean
    public SpreadsheetWriter spreadsheetWriter(FileSystem fileSystem) {
        return new XLSXSpreadsheetWriter(fileSystem);
    }

    @Bean
    public Deserializer<Profile> profileDeserializer(org.apache.hadoop.conf.Configuration configuration,
                                                     DirectoryUtils directoryUtils) {
        return new AvroProfileDeserializer(configuration, directoryUtils);
    }
}
