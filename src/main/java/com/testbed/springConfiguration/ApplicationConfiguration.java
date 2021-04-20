package com.testbed.springConfiguration;

import com.clearspring.analytics.util.Lists;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.testbed.boundary.deserializers.AvroProfileDeserializer;
import com.testbed.boundary.deserializers.DeserializedOperationMixin;
import com.testbed.boundary.deserializers.Deserializer;
import com.testbed.boundary.deserializers.JsonOperationsDeserializer;
import com.testbed.boundary.invocations.instrumentation.OperationInstrumentation;
import com.testbed.boundary.readers.AvroColumnReader;
import com.testbed.boundary.readers.ColumnReader;
import com.testbed.boundary.serializers.CSVSerializer;
import com.testbed.entities.operations.deserialized.DeserializedOperation;
import com.testbed.entities.operations.deserialized.DeserializedOperations;
import com.testbed.entities.profiles.Profile;
import com.testbed.interactors.InteractorFactory;
import com.testbed.interactors.converters.deserializedToLogical.DeserializedToLogicalConverterManager;
import com.testbed.interactors.converters.logicalToPhysical.LogicalToPhysicalConverterManager;
import com.testbed.interactors.invokers.InvocationPlanner;
import com.testbed.interactors.invokers.InvokerManager;
import com.testbed.interactors.validators.semantic.InputsCountValidatorManager;
import com.testbed.interactors.validators.syntactic.NotNullOnAllFieldsValidatorManager;
import com.testbed.interactors.viewers.InvocationInstrumentationViewer;
import com.testbed.views.InvocationInstrumentationView;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ApplicationConfiguration {
    private static final String OBJECT_MAPPER_WITH_DESERIALIZED_OPERATION_MIXIN = "objectMapperWithDeserializedOperationMixin";
    private static final String OBJECT_MAPPER_WITH_JAVA_TIME_MODULE = "objectMapperWithJavaTimeModule";

    @Bean
    public InteractorFactory interactorFactory(Deserializer<DeserializedOperations> operationsDeserializer,
                                               NotNullOnAllFieldsValidatorManager notNullOnAllFieldsValidatorManager,
                                               DeserializedToLogicalConverterManager deserializedToLogicalConverterManager,
                                               InputsCountValidatorManager inputsCountValidatorManager,
                                               LogicalToPhysicalConverterManager logicalToPhysicalConverterManager,
                                               InvocationPlanner invocationPlanner,
                                               InvokerManager invokerManager,
                                               List<OperationInstrumentation> operationInstrumentations,
                                               InvocationInstrumentationViewer invocationInstrumentationViewer) {
        return new InteractorFactory(operationsDeserializer,
                notNullOnAllFieldsValidatorManager,
                deserializedToLogicalConverterManager,
                inputsCountValidatorManager,
                logicalToPhysicalConverterManager,
                invocationPlanner,
                invokerManager,
                operationInstrumentations,
                invocationInstrumentationViewer);
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
    public InvocationInstrumentationViewer invocationInstrumentationViewer(
            CSVSerializer<InvocationInstrumentationView> invocationInstrumentationViewCSVSerializer,
            @Qualifier(OBJECT_MAPPER_WITH_JAVA_TIME_MODULE) ObjectMapper objectMapper) {
        return new InvocationInstrumentationViewer(invocationInstrumentationViewCSVSerializer, objectMapper);
    }

    @Bean
    @Qualifier(OBJECT_MAPPER_WITH_JAVA_TIME_MODULE)
    public ObjectMapper objectMapper() {
        return JsonMapper.builder().build();
    }

    @Bean
    public ColumnReader getColumnReader() {
        return new AvroColumnReader();
    }

    @Bean
    public CSVSerializer<InvocationInstrumentationView> invocationInstrumentationViewCSVSerializer() {
        return new CSVSerializer<>(InvocationInstrumentationView.class);
    }

    @Bean
    public Deserializer<Profile> profileDeserializer() {
        return new AvroProfileDeserializer();
    }
}
