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
import com.testbed.boundary.invocations.spark.GroupByInvokable;
import com.testbed.boundary.invocations.spark.JoinInvokable;
import com.testbed.boundary.invocations.spark.LoadInvokable;
import com.testbed.boundary.invocations.spark.ProjectInvokable;
import com.testbed.boundary.invocations.spark.SelectInvokable;
import com.testbed.boundary.invocations.spark.SinkInvokable;
import com.testbed.boundary.readers.AvroColumnReader;
import com.testbed.boundary.readers.ColumnReader;
import com.testbed.boundary.serializers.CSVSerializer;
import com.testbed.entities.operations.deserialized.DeserializedGroupBy;
import com.testbed.entities.operations.deserialized.DeserializedJoin;
import com.testbed.entities.operations.deserialized.DeserializedLoad;
import com.testbed.entities.operations.deserialized.DeserializedOperation;
import com.testbed.entities.operations.deserialized.DeserializedOperations;
import com.testbed.entities.operations.deserialized.DeserializedProject;
import com.testbed.entities.operations.deserialized.DeserializedSelect;
import com.testbed.entities.profiles.Profile;
import com.testbed.factories.InteractorFactory;
import com.testbed.interactors.converters.deserializedToLogical.DeserializedToLogicalGroupByConverter;
import com.testbed.interactors.converters.deserializedToLogical.DeserializedToLogicalJoinConverter;
import com.testbed.interactors.converters.deserializedToLogical.DeserializedToLogicalLoadConverter;
import com.testbed.interactors.converters.deserializedToLogical.DeserializedToLogicalOperationConverter;
import com.testbed.interactors.converters.deserializedToLogical.DeserializedToLogicalOperationsConverter;
import com.testbed.interactors.converters.deserializedToLogical.DeserializedToLogicalProjectConverter;
import com.testbed.interactors.converters.deserializedToLogical.DeserializedToLogicalSelectConverter;
import com.testbed.interactors.converters.dispatchers.Dispatcher;
import com.testbed.interactors.converters.dispatchers.DispatchersFactory;
import com.testbed.interactors.converters.dispatchers.FilterInDeserializedLoadDispatcher;
import com.testbed.interactors.converters.dispatchers.GroupByInputTagStreamDispatcher;
import com.testbed.interactors.converters.dispatchers.JoinInputTagStreamDispatcher;
import com.testbed.interactors.converters.dispatchers.ProjectInputTagStreamDispatcher;
import com.testbed.interactors.converters.dispatchers.SelectInputTagStreamDispatcher;
import com.testbed.interactors.converters.logicalToPhysical.LogicalToPhysicalGroupByConverter;
import com.testbed.interactors.converters.logicalToPhysical.LogicalToPhysicalJoinConverter;
import com.testbed.interactors.converters.logicalToPhysical.LogicalToPhysicalLoadConverter;
import com.testbed.interactors.converters.logicalToPhysical.LogicalToPhysicalOperationConverter;
import com.testbed.interactors.converters.logicalToPhysical.LogicalToPhysicalOperationsConverter;
import com.testbed.interactors.converters.logicalToPhysical.LogicalToPhysicalProjectConverter;
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
import java.util.stream.Stream;

@Configuration
public class SpringConfiguration {
    private static final String DESERIALIZED_TO_LOGICAL_CONVERTERS_MAPPING = "deserializedToLogicalConvertersMapping";
    private static final String LOGICAL_CONVERTERS_MAPPING = "logicalConvertersMapping";
    private static final String SPARK_PHYSICAL_OPERATION_TO_INVOKABLE_MAPPING = "sparkPhysicalOperationToInvokableMapping";
    private static final String SPARK_PHYSICAL_OPERATION_TO_INSTRUMENT_INVOKABLE_MAPPING = "sparkPhysicalOperationToInstrumentInvokableMapping";

    private static final String SPARK_INVOKER = "sparkInvoker";

    private static final String APP_NAME = "Testbed";
    private static final String LOCAL = "local[*]";
    private static final String OBJECT_MAPPER_WITH_DESERIALIZED_OPERATION_MIXIN = "objectMapperWithDeserializedOperationMixin";
    private static final String OBJECT_MAPPER_WITH_JAVA_TIME_MODULE = "objectMapperWithJavaTimeModule";

    private static final String DESERIALIZED_TO_LOGICAL_LOAD_CONVERTER = "deserializedToLogicalLoadConverter";
    private static final String DESERIALIZED_TO_LOGICAL_SELECT_CONVERTER = "deserializedToLogicalSelectConverter";
    private static final String DESERIALIZED_TO_LOGICAL_PROJECT_CONVERTER = "deserializedToLogicalProjectConverter";
    private static final String DESERIALIZED_TO_LOGICAL_JOIN_CONVERTER = "deserializedToLogicalJoinConverter";
    private static final String DESERIALIZED_TO_LOGICAL_GROUP_BY_CONVERTER = "deserializedToLogicalGroupByConverter";

    private static final String LOGICAL_TO_PHYSICAL_LOAD_CONVERTER = "logicalToPhysicalLoadConverter";
    private static final String LOGICAL_TO_PHYSICAL_SELECT_CONVERTER = "logicalToPhysicalSelectConverter";
    private static final String LOGICAL_TO_PHYSICAL_PROJECT_CONVERTER = "logicalToPhysicalProjectConverter";
    private static final String LOGICAL_TO_PHYSICAL_JOIN_CONVERTER = "logicalToPhysicalJoinConverter";
    private static final String LOGICAL_TO_PHYSICAL_GROUP_BY_CONVERTER = "logicalToPhysicalGroupByConverter";

    private static final String DESERIALIZED_LOAD = "DeserializedLoad";
    private static final String DESERIALIZED_SELECT = "DeserializedSelect";
    private static final String DESERIALIZED_PROJECT = "DeserializedProject";
    private static final String DESERIALIZED_JOIN = "DeserializedJoin";
    private static final String DESERIALIZED_GROUP_BY = "DeserializedGroupBy";

    private static final String LOGICAL_LOAD = "LogicalLoad";
    private static final String LOGICAL_SELECT = "LogicalSelect";
    private static final String LOGICAL_PROJECT = "LogicalProject";
    private static final String LOGICAL_JOIN = "LogicalJoin";
    private static final String LOGICAL_GROUP_BY = "LogicalGroupBy";

    private static final String PHYSICAL_LOAD = "PhysicalLoad";
    private static final String PHYSICAL_SELECT = "PhysicalSelect";
    private static final String PHYSICAL_PROJECT = "PhysicalProject";
    private static final String PHYSICAL_JOIN = "PhysicalJoin";
    private static final String PHYSICAL_GROUP_BY = "PhysicalGroupBy";
    private static final String PHYSICAL_SINK = "PhysicalSink";

    private static final String SPARK_LOAD_INVOKABLE = "sparkLoadInvokable";
    private static final String SPARK_SELECT_INVOKABLE = "sparkSelectInvokable";
    private static final String SPARK_PROJECT_INVOKABLE = "sparkProjectInvokable";
    private static final String SPARK_JOIN_INVOKABLE = "sparkJoinInvokable";
    private static final String SPARK_GROUP_BY_INVOKABLE = "sparkGroupByInvokable";
    private static final String SPARK_SINK_INVOKABLE = "sparkSinkInvokable";

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

    @Bean(name = OBJECT_MAPPER_WITH_DESERIALIZED_OPERATION_MIXIN)
    public ObjectMapper getObjectMapperWithDeserializedOperationMixin() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.addMixIn(DeserializedOperation.class, DeserializedOperationMixin.class);
        return objectMapper;
    }

    @Bean
    public DeserializedToLogicalOperationsConverter getDeserializedToLogicalOperationsConverter() {
        return new DeserializedToLogicalOperationsConverter(getDeserializedToLogicalConvertersMapping(),
                getDispatchersFactory());
    }

    @Bean(name = DESERIALIZED_TO_LOGICAL_CONVERTERS_MAPPING)
    public Map<String, DeserializedToLogicalOperationConverter> getDeserializedToLogicalConvertersMapping() {
        return Maps.newHashMap(ImmutableMap.of(
                DESERIALIZED_LOAD, getDeserializedToLogicalLoadConverter(),
                DESERIALIZED_SELECT, getDeserializedToLogicalSelectConverter(),
                DESERIALIZED_PROJECT, getDeserializedToLogicalProjectConverter(),
                DESERIALIZED_JOIN, getDeserializedToLogicalJoinConverter(),
                DESERIALIZED_GROUP_BY, getDeserializedToLogicalGroupByConverter()
        ));
    }

    @Bean(name = DESERIALIZED_TO_LOGICAL_LOAD_CONVERTER)
    public DeserializedToLogicalOperationConverter getDeserializedToLogicalLoadConverter() {
        return new DeserializedToLogicalLoadConverter();
    }

    @Bean(name = DESERIALIZED_TO_LOGICAL_SELECT_CONVERTER)
    public DeserializedToLogicalOperationConverter getDeserializedToLogicalSelectConverter() {
        return new DeserializedToLogicalSelectConverter();
    }

    @Bean(name = DESERIALIZED_TO_LOGICAL_PROJECT_CONVERTER)
    public DeserializedToLogicalProjectConverter getDeserializedToLogicalProjectConverter() {
        return new DeserializedToLogicalProjectConverter();
    }

    @Bean(name = DESERIALIZED_TO_LOGICAL_JOIN_CONVERTER)
    public DeserializedToLogicalOperationConverter getDeserializedToLogicalJoinConverter() {
        return new DeserializedToLogicalJoinConverter();
    }

    @Bean(name = DESERIALIZED_TO_LOGICAL_GROUP_BY_CONVERTER)
    public DeserializedToLogicalOperationConverter getDeserializedToLogicalGroupByConverter() {
        return new DeserializedToLogicalGroupByConverter();
    }

    @Bean
    public DispatchersFactory getDispatchersFactory() {
        return new DispatchersFactory(getFilterInDeserializedLoadDispatcher(),
                getSelectInputTagStreamDispatcher(),
                getProjectStreamDispatcher(),
                getJoinStreamDispatcher(),
                getGroupByStreamDispatcher());
    }

    @Bean
    public Dispatcher<DeserializedLoad, DeserializedLoad> getFilterInDeserializedLoadDispatcher() {
        return new FilterInDeserializedLoadDispatcher();
    }

    @Bean
    public Dispatcher<DeserializedSelect, Stream<String>> getSelectInputTagStreamDispatcher() {
        return new SelectInputTagStreamDispatcher();
    }

    @Bean
    public Dispatcher<DeserializedProject, Stream<String>> getProjectStreamDispatcher() {
        return new ProjectInputTagStreamDispatcher();
    }

    @Bean
    public Dispatcher<DeserializedJoin, Stream<String>> getJoinStreamDispatcher() {
        return new JoinInputTagStreamDispatcher();
    }

    @Bean
    public Dispatcher<DeserializedGroupBy, Stream<String>> getGroupByStreamDispatcher() {
        return new GroupByInputTagStreamDispatcher();
    }

    @Bean
    public LogicalToPhysicalOperationsConverter getLogicalOperationsConverter() {
        return new LogicalToPhysicalOperationsConverter(getProfileDeserializer(), getLogicalConvertersMapping());
    }

    @Bean
    public Deserializer<Profile> getProfileDeserializer() {
        return new AvroProfileDeserializer();
    }

    @Bean(name = LOGICAL_CONVERTERS_MAPPING)
    public Map<String, LogicalToPhysicalOperationConverter> getLogicalConvertersMapping() {
        return Map.of(
                LOGICAL_LOAD, getLogicalToPhysicalLoadConverter(),
                LOGICAL_SELECT, getLogicalToPhysicalSelectConverter(),
                LOGICAL_PROJECT, getLogicalToPhysicalProjectConverter(),
                LOGICAL_JOIN, getLogicalToPhysicalJoinConverter(),
                LOGICAL_GROUP_BY, getLogicalToPhysicalGroupByConverter()
        );
    }

    @Bean(name = LOGICAL_TO_PHYSICAL_LOAD_CONVERTER)
    public LogicalToPhysicalOperationConverter getLogicalToPhysicalLoadConverter() {
        return new LogicalToPhysicalLoadConverter();
    }

    @Bean(name = LOGICAL_TO_PHYSICAL_SELECT_CONVERTER)
    public LogicalToPhysicalOperationConverter getLogicalToPhysicalSelectConverter() {
        return new LogicalToPhysicalSelectConverter(getColumnReader());
    }

    @Bean(name = LOGICAL_TO_PHYSICAL_PROJECT_CONVERTER)
    public LogicalToPhysicalProjectConverter getLogicalToPhysicalProjectConverter() {
        return new LogicalToPhysicalProjectConverter();
    }

    @Bean(name = LOGICAL_TO_PHYSICAL_JOIN_CONVERTER)
    public LogicalToPhysicalOperationConverter getLogicalToPhysicalJoinConverter() {
        return new LogicalToPhysicalJoinConverter();
    }

    @Bean(name = LOGICAL_TO_PHYSICAL_GROUP_BY_CONVERTER)
    public LogicalToPhysicalOperationConverter getLogicalToPhysicalGroupByConverter() {
        return new LogicalToPhysicalGroupByConverter();
    }

    @Bean
    public JobCreator getJobCreator() {
        return new JobCreator();
    }

    @Bean(name = SPARK_INVOKER)
    public JobInvoker getSparkInvoker() {
        return new JobInvoker(getSparkPhysicalOperationToInstrumentInvokableMapper());
    }

    @Bean(name = SPARK_PHYSICAL_OPERATION_TO_INSTRUMENT_INVOKABLE_MAPPING)
    public Map<String, Invokable> getSparkPhysicalOperationToInstrumentInvokableMapper() {
        return Map.of(
                PHYSICAL_LOAD, getInstrumentInvokable(getSparkLoadInvokable()),
                PHYSICAL_SELECT, getInstrumentInvokable(getSparkSelectInvokable()),
                PHYSICAL_PROJECT, getInstrumentInvokable(getSparkProjectInvokable()),
                PHYSICAL_JOIN, getInstrumentInvokable(getSparkJoinInvokable()),
                PHYSICAL_GROUP_BY, getInstrumentInvokable(getSparkGroupByInvokable()),
                PHYSICAL_SINK, getInstrumentInvokable(getSparkSinkInvokable())
        );
    }

    public Invokable getInstrumentInvokable(Invokable wrappedInvokable) {
        return new InstrumentInvokable(wrappedInvokable, getOperationInstrumentations());
    }

    @Bean(name = SPARK_LOAD_INVOKABLE)
    public Invokable getSparkLoadInvokable() {
        return new LoadInvokable(getSparkSession());
    }

    @Bean(name = SPARK_SELECT_INVOKABLE)
    public Invokable getSparkSelectInvokable() {
        return new SelectInvokable();
    }

    @Bean(name = SPARK_PROJECT_INVOKABLE)
    public Invokable getSparkProjectInvokable() {
        return new ProjectInvokable();
    }

    @Bean(name = SPARK_JOIN_INVOKABLE)
    public Invokable getSparkJoinInvokable() {
        return new JoinInvokable();
    }

    @Bean(name = SPARK_GROUP_BY_INVOKABLE)
    public Invokable getSparkGroupByInvokable() {
        return new GroupByInvokable();
    }

    @Bean(name = SPARK_SINK_INVOKABLE)
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

    @Bean(name = SPARK_PHYSICAL_OPERATION_TO_INVOKABLE_MAPPING)
    public Map<String, Invokable> getSparkPhysicalOperationToInvokableMapper() {
        return Map.of(
                PHYSICAL_LOAD, getSparkLoadInvokable(),
                PHYSICAL_SELECT, getSparkSelectInvokable(),
                PHYSICAL_PROJECT, getSparkProjectInvokable(),
                PHYSICAL_JOIN, getSparkJoinInvokable(),
                PHYSICAL_GROUP_BY, getSparkGroupByInvokable(),
                PHYSICAL_SINK, getSparkSinkInvokable()
        );
    }

    @Bean
    public InvocationInstrumentationViewer getInvocationInstrumentationViewer() {
        return new InvocationInstrumentationViewer(getInvocationInstrumentationViewCSVSerializer(), getObjectMapperWithJavaTimeModule());
    }

    @Bean(name = OBJECT_MAPPER_WITH_JAVA_TIME_MODULE)
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
