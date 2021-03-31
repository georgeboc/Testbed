package com.testbed.configuration;

import com.clearspring.analytics.util.Lists;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.testbed.boundary.deserializers.AvroProfileDeserializer;
import com.testbed.boundary.deserializers.DeserializedOperationMixin;
import com.testbed.boundary.deserializers.Deserializer;
import com.testbed.boundary.deserializers.JsonOperationsDeserializer;
import com.testbed.boundary.invocations.InstrumentInvokable;
import com.testbed.boundary.invocations.Invokable;
import com.testbed.boundary.invocations.OperationInstrumentation;
import com.testbed.boundary.invocations.spark.LoadInvokable;
import com.testbed.boundary.invocations.spark.SelectInvokable;
import com.testbed.boundary.invocations.spark.SinkInvokable;
import com.testbed.boundary.readers.AvroColumnReader;
import com.testbed.boundary.readers.ColumnReader;
import com.testbed.boundary.serializers.CSVSerializer;
import com.testbed.entities.operations.deserialized.DeserializedOperation;
import com.testbed.entities.operations.deserialized.DeserializedOperations;
import com.testbed.entities.profiles.Profile;
import com.testbed.factories.InteractorFactory;
import com.testbed.interactors.converters.deserializedToLogical.DeserializedToLogicalLoadConverter;
import com.testbed.interactors.converters.deserializedToLogical.DeserializedToLogicalOperationConverter;
import com.testbed.interactors.converters.deserializedToLogical.DeserializedToLogicalOperationsConverter;
import com.testbed.interactors.converters.deserializedToLogical.DeserializedToLogicalSelectConverter;
import com.testbed.interactors.converters.logicalToPhysical.LogicalToPhysicalLoadConverter;
import com.testbed.interactors.converters.logicalToPhysical.LogicalToPhysicalOperationConverter;
import com.testbed.interactors.converters.logicalToPhysical.LogicalToPhysicalOperationsConverter;
import com.testbed.interactors.converters.logicalToPhysical.LogicalToPhysicalSelectConverter;
import com.testbed.interactors.jobs.JobCreator;
import com.testbed.interactors.jobs.JobInvoker;
import com.testbed.interactors.viewers.InvocationInstrumentationViewer;
import com.testbed.views.InvocationInstrumentationView;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.sql.SparkSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
public class SpringConfiguration {
    private static final String DESERIALIZED_TO_LOGICAL_SELECT_CONVERTER = "deserializedToLogicalSelectConverter";
    private static final String DESERIALIZED_TO_LOGICAL_LOAD_CONVERTER = "deserializedToLogicalLoadConverter";

    private static final String LOGICAL_SELECT_CONVERTER = "logicalSelectConverter";
    private static final String LOGICAL_LOAD_CONVERTER = "logicalLoadConverter";

    private static final String DESERIALIZED_TO_LOGICAL_CONVERTERS_MAPPING = "deserializedToLogicalConvertersMapping";
    private static final String LOGICAL_CONVERTERS_MAPPING = "logicalConvertersMapping";
    private static final String SPARK_PHYSICAL_OPERATION_TO_INVOKABLE_MAPPING = "sparkPhysicalOperationToInvokableMapping";
    private static final String SPARK_PHYSICAL_OPERATION_TO_INSTRUMENT_INVOKABLE_MAPPING = "sparkPhysicalOperationToInstrumentInvokableMapping";

    private static final String DESERIALIZED_SELECT = "DeserializedSelect";
    private static final String DESERIALIZED_LOAD = "DeserializedLoad";

    private static final String LOGICAL_SELECT = "LogicalSelect";
    private static final String LOGICAL_LOAD = "LogicalLoad";

    private static final String PHYSICAL_LOAD = "PhysicalLoad";
    private static final String PHYSICAL_SELECT = "PhysicalSelect";
    private static final String PHYSICAL_SINK = "PhysicalSink";

    private static final String SPARK_LOAD_INVOKABLE = "sparkLoadInvokable";
    private static final String SPARK_SELECT_INVOKABLE = "sparkSelectInvokable";
    private static final String SPARK_SINK_INVOKABLE = "sparkSinkInvokable";

    private static final String SPARK_INVOKER = "sparkInvoker";
    
    private static final String APP_NAME = "Testbed";
    private static final String LOCAL = "local[*]";
    private static final String OBJECT_MAPPER_WITH_DESERIALIZED_OPERATION_MIXIN = "objectMapperWithDeserializedOperationMixin";
    private static final String OBJECT_MAPPER_WITH_JAVA_TIME_MODULE = "objectMapperWithJavaTimeModule";

    @Bean
    public ColumnReader getColumnReader() {
        return new AvroColumnReader();
    }

    @Bean
    public InteractorFactory getReadJsonAndPrintContentFactory() {
        return new InteractorFactory(getOperationsDeserializer(),
                getDeserializedToLogicalOperationsConverter(),
                getLogicalOperationsConverter(),
                getJobCreator(),
                getSparkInvoker(),
                getOperationInstrumentations(),
                getInvocationInstrumentationViewer());
    }

    @Bean
    public Deserializer<DeserializedOperations> getOperationsDeserializer() {
        return new JsonOperationsDeserializer(getObjectMapperWithDeserializedOperationMixin());
    }

    @Bean(name= OBJECT_MAPPER_WITH_DESERIALIZED_OPERATION_MIXIN)
    public ObjectMapper getObjectMapperWithDeserializedOperationMixin() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.addMixIn(DeserializedOperation.class, DeserializedOperationMixin.class);
        return objectMapper;
    }

    @Bean
    public DeserializedToLogicalOperationsConverter getDeserializedToLogicalOperationsConverter() {
        return new DeserializedToLogicalOperationsConverter(getDeserializedToLogicalConvertersMapping());
    }

    @Bean(name= DESERIALIZED_TO_LOGICAL_CONVERTERS_MAPPING)
    public Map<String, DeserializedToLogicalOperationConverter> getDeserializedToLogicalConvertersMapping() {
        return Maps.newHashMap(ImmutableMap.of(
                DESERIALIZED_LOAD, getDeserializedToLogicalLoadConverter(),
                DESERIALIZED_SELECT, getDeserializedToLogicalSelectConverter()
        ));
    }

    @Bean(name= DESERIALIZED_TO_LOGICAL_LOAD_CONVERTER)
    public DeserializedToLogicalOperationConverter getDeserializedToLogicalLoadConverter() {
        return new DeserializedToLogicalLoadConverter();
    }

    @Bean(name= DESERIALIZED_TO_LOGICAL_SELECT_CONVERTER)
    public DeserializedToLogicalOperationConverter getDeserializedToLogicalSelectConverter() {
        return new DeserializedToLogicalSelectConverter();
    }

    @Bean
    public LogicalToPhysicalOperationsConverter getLogicalOperationsConverter() {
        return new LogicalToPhysicalOperationsConverter(getProfileDeserializer(), getLogicalConvertersMapping());
    }

    @Bean
    public Deserializer<Profile> getProfileDeserializer() {
        return new AvroProfileDeserializer();
    }

    @Bean(name=LOGICAL_CONVERTERS_MAPPING)
    public Map<String, LogicalToPhysicalOperationConverter> getLogicalConvertersMapping() {
        return Maps.newHashMap(ImmutableMap.of(
                LOGICAL_LOAD, getLogicalLoadConverter(),
                LOGICAL_SELECT, getLogicalSelectConverter()
        ));
    }

    @Bean(name=LOGICAL_LOAD_CONVERTER)
    public LogicalToPhysicalOperationConverter getLogicalLoadConverter() {
        return new LogicalToPhysicalLoadConverter();
    }

    @Bean(name=LOGICAL_SELECT_CONVERTER)
    public LogicalToPhysicalOperationConverter getLogicalSelectConverter() {
        return new LogicalToPhysicalSelectConverter(getColumnReader());
    }

    @Bean
    public JobCreator getJobCreator() {
        return new JobCreator();
    }
    
    @Bean(name= SPARK_INVOKER)
    public JobInvoker getSparkInvoker() {
        return new JobInvoker(getSparkPhysicalOperationToInstrumentInvokableMapper());
    }

    @Bean(name= SPARK_PHYSICAL_OPERATION_TO_INSTRUMENT_INVOKABLE_MAPPING)
    public Map<String, Invokable> getSparkPhysicalOperationToInstrumentInvokableMapper() {
        return Maps.newHashMap(ImmutableMap.of(
                PHYSICAL_LOAD, getInstrumentInvokable(getSparkLoadInvokable()),
                PHYSICAL_SELECT, getInstrumentInvokable(getSparkSelectInvokable()),
                PHYSICAL_SINK, getInstrumentInvokable(getSparkSinkInvokable())
        ));
    }

    public Invokable getInstrumentInvokable(Invokable wrappedInvokable) {
        return new InstrumentInvokable(wrappedInvokable, getOperationInstrumentations());
    }

    @Bean(name= SPARK_LOAD_INVOKABLE)
    public Invokable getSparkLoadInvokable() {
        return new LoadInvokable(getSparkSession());
    }

    @Bean(name= SPARK_SELECT_INVOKABLE)
    public Invokable getSparkSelectInvokable() {
        return new SelectInvokable();
    }

    @Bean(name= SPARK_SINK_INVOKABLE)
    public Invokable getSparkSinkInvokable() {
        return new SinkInvokable();
    }

    @Bean
    public List<OperationInstrumentation> getOperationInstrumentations() {
        return Lists.newArrayList();
    }

    @Bean
    public SparkSession getSparkSession() {
        SparkConf conf = new SparkConf().setAppName(APP_NAME).setMaster(LOCAL);
        SparkContext sparkContext = new SparkContext(conf);
        return new SparkSession(sparkContext);
    }

    @Bean(name= SPARK_PHYSICAL_OPERATION_TO_INVOKABLE_MAPPING)
    public Map<String, Invokable> getSparkPhysicalOperationToInvokableMapper() {
        return Maps.newHashMap(ImmutableMap.of(
                PHYSICAL_LOAD, getSparkLoadInvokable(),
                PHYSICAL_SELECT, getSparkSelectInvokable(),
                PHYSICAL_SINK, getSparkSinkInvokable()
        ));
    }

    @Bean
    public InvocationInstrumentationViewer getInvocationInstrumentationViewer() {
        return new InvocationInstrumentationViewer(getInvocationInstrumentationViewCSVSerializer(), getObjectMapperWithJavaTimeModule());
    }

    @Bean(name= OBJECT_MAPPER_WITH_JAVA_TIME_MODULE)
    public ObjectMapper getObjectMapperWithJavaTimeModule() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    @Bean
    public CSVSerializer<InvocationInstrumentationView> getInvocationInstrumentationViewCSVSerializer() {
        return new CSVSerializer<>(InvocationInstrumentationView.class);
    }
}
