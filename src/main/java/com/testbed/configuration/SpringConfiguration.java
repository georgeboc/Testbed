package com.testbed.configuration;

import com.clearspring.analytics.util.Lists;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.testbed.boundary.deserializers.AvroProfileDeserializer;
import com.testbed.boundary.deserializers.DeserializedOperationMixin;
import com.testbed.boundary.deserializers.Deserializer;
import com.testbed.boundary.deserializers.JsonOperationsDeserializer;
import com.testbed.boundary.invocations.InstrumentInvokable;
import com.testbed.boundary.invocations.Invokable;
import com.testbed.boundary.invocations.OperationInstrumentation;
import com.testbed.boundary.invocations.spark.AggregateSparkInvokable;
import com.testbed.boundary.invocations.spark.GroupBySparkInvokable;
import com.testbed.boundary.invocations.spark.JoinSparkInvokable;
import com.testbed.boundary.invocations.spark.LoadSparkInvokable;
import com.testbed.boundary.invocations.spark.ProjectSparkInvokable;
import com.testbed.boundary.invocations.spark.SelectSparkInvokable;
import com.testbed.boundary.invocations.spark.SinkSparkInvokable;
import com.testbed.boundary.invocations.spark.UnionSparkInvokable;
import com.testbed.boundary.readers.AvroColumnReader;
import com.testbed.boundary.readers.ColumnReader;
import com.testbed.boundary.serializers.CSVSerializer;
import com.testbed.entities.operations.deserialized.BinaryDeserializedOperation;
import com.testbed.entities.operations.deserialized.DeserializedLoad;
import com.testbed.entities.operations.deserialized.DeserializedOperation;
import com.testbed.entities.operations.deserialized.DeserializedOperations;
import com.testbed.entities.operations.deserialized.UnaryDeserializedOperation;
import com.testbed.entities.profiles.Profile;
import com.testbed.interactors.InteractorFactory;
import com.testbed.interactors.converters.deserializedToLogical.AggregateDeserializedToLogicalConverter;
import com.testbed.interactors.converters.deserializedToLogical.GroupByDeserializedToLogicalConverter;
import com.testbed.interactors.converters.deserializedToLogical.JoinDeserializedToLogicalConverter;
import com.testbed.interactors.converters.deserializedToLogical.LoadDeserializedToLogicalConverter;
import com.testbed.interactors.converters.deserializedToLogical.DeserializedToLogicalConverter;
import com.testbed.interactors.converters.deserializedToLogical.DeserializedToLogicalManager;
import com.testbed.interactors.converters.deserializedToLogical.ProjectDeserializedToLogicalConverter;
import com.testbed.interactors.converters.deserializedToLogical.SelectDeserializedToLogicalConverter;
import com.testbed.interactors.converters.deserializedToLogical.UnionDeserializedToLogicalConverter;
import com.testbed.interactors.converters.dispatchers.BinaryInputTagStreamDispatcher;
import com.testbed.interactors.converters.dispatchers.Dispatcher;
import com.testbed.interactors.converters.dispatchers.DispatchersFactory;
import com.testbed.interactors.converters.dispatchers.FilterInDeserializedLoadDispatcher;
import com.testbed.interactors.converters.dispatchers.UnaryInputTagStreamDispatcher;
import com.testbed.interactors.converters.logicalToPhysical.AggregateLogicalToPhysicalConverter;
import com.testbed.interactors.converters.logicalToPhysical.GroupByLogicalToPhysicalConverter;
import com.testbed.interactors.converters.logicalToPhysical.JoinLogicalToPhysicalConverter;
import com.testbed.interactors.converters.logicalToPhysical.LoadLogicalToPhysicalConverter;
import com.testbed.interactors.converters.logicalToPhysical.LogicalToPhysicalConverter;
import com.testbed.interactors.converters.logicalToPhysical.LogicalToPhysicalManager;
import com.testbed.interactors.converters.logicalToPhysical.ProjectLogicalToPhysicalConverter;
import com.testbed.interactors.converters.logicalToPhysical.SelectLogicalToPhysicalConverter;
import com.testbed.interactors.converters.logicalToPhysical.UnionLogicalToPhysicalConverter;
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
    private static final String DESERIALIZED_TO_LOGICAL_AGGREGATE_CONVERTER = "deserializedToLogicalAggregateConverter";
    private static final String DESERIALIZED_TO_LOGICAL_UNION_CONVERTER = "deserializedToLogicalUnionConverter";

    private static final String LOGICAL_TO_PHYSICAL_LOAD_CONVERTER = "logicalToPhysicalLoadConverter";
    private static final String LOGICAL_TO_PHYSICAL_SELECT_CONVERTER = "logicalToPhysicalSelectConverter";
    private static final String LOGICAL_TO_PHYSICAL_PROJECT_CONVERTER = "logicalToPhysicalProjectConverter";
    private static final String LOGICAL_TO_PHYSICAL_JOIN_CONVERTER = "logicalToPhysicalJoinConverter";
    private static final String LOGICAL_TO_PHYSICAL_GROUP_BY_CONVERTER = "logicalToPhysicalGroupByConverter";
    private static final String LOGICAL_TO_PHYSICAL_AGGREGATE_CONVERTER = "logicalToPhysicalAggregateConverter";
    private static final String LOGICAL_TO_PHYSICAL_UNION_CONVERTER = "logicalToPhysicalUnionConverter";

    private static final String DESERIALIZED_LOAD = "DeserializedLoad";
    private static final String DESERIALIZED_SELECT = "DeserializedSelect";
    private static final String DESERIALIZED_PROJECT = "DeserializedProject";
    private static final String DESERIALIZED_JOIN = "DeserializedJoin";
    private static final String DESERIALIZED_GROUP_BY = "DeserializedGroupBy";
    private static final String DESERIALIZED_AGGREGATE = "DeserializedAggregate";
    private static final String DESERIALIZED_UNION = "DeserializedUnion";

    private static final String LOGICAL_LOAD = "LogicalLoad";
    private static final String LOGICAL_SELECT = "LogicalSelect";
    private static final String LOGICAL_PROJECT = "LogicalProject";
    private static final String LOGICAL_JOIN = "LogicalJoin";
    private static final String LOGICAL_GROUP_BY = "LogicalGroupBy";
    private static final String LOGICAL_AGGREGATE = "LogicalAggregate";
    private static final String LOGICAL_UNION = "LogicalUnion";

    private static final String PHYSICAL_LOAD = "PhysicalLoad";
    private static final String PHYSICAL_SELECT = "PhysicalSelect";
    private static final String PHYSICAL_PROJECT = "PhysicalProject";
    private static final String PHYSICAL_JOIN = "PhysicalJoin";
    private static final String PHYSICAL_GROUP_BY = "PhysicalGroupBy";
    private static final String PHYSICAL_AGGREGATE = "PhysicalAggregate";
    private static final String PHYSICAL_UNION = "PhysicalUnion";
    private static final String PHYSICAL_SINK = "PhysicalSink";

    private static final String SPARK_LOAD_INVOKABLE = "sparkLoadInvokable";
    private static final String SPARK_SELECT_INVOKABLE = "sparkSelectInvokable";
    private static final String SPARK_PROJECT_INVOKABLE = "sparkProjectInvokable";
    private static final String SPARK_JOIN_INVOKABLE = "sparkJoinInvokable";
    private static final String SPARK_GROUP_BY_INVOKABLE = "sparkGroupByInvokable";
    private static final String SPARK_AGGREGATE_INVOKABLE = "sparkAggregateInvokable";
    private static final String SPARK_UNION_INVOKABLE = "sparkUnionInvokable";
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
    public DeserializedToLogicalManager getDeserializedToLogicalOperationsConverter() {
        return new DeserializedToLogicalManager(getDeserializedToLogicalConvertersMapping(),
                getDispatchersFactory());
    }

    @Bean(name = DESERIALIZED_TO_LOGICAL_CONVERTERS_MAPPING)
    public Map<String, DeserializedToLogicalConverter> getDeserializedToLogicalConvertersMapping() {
        return Map.of(
                DESERIALIZED_LOAD, getDeserializedToLogicalLoadConverter(),
                DESERIALIZED_SELECT, getDeserializedToLogicalSelectConverter(),
                DESERIALIZED_PROJECT, getDeserializedToLogicalProjectConverter(),
                DESERIALIZED_JOIN, getDeserializedToLogicalJoinConverter(),
                DESERIALIZED_GROUP_BY, getDeserializedToLogicalGroupByConverter(),
                DESERIALIZED_AGGREGATE, getDeserializedToLogicalAggregateConverter(),
                DESERIALIZED_UNION, getDeserializedToLogicalUnionConverter()
        );
    }

    @Bean(name = DESERIALIZED_TO_LOGICAL_LOAD_CONVERTER)
    public DeserializedToLogicalConverter getDeserializedToLogicalLoadConverter() {
        return new LoadDeserializedToLogicalConverter();
    }

    @Bean(name = DESERIALIZED_TO_LOGICAL_SELECT_CONVERTER)
    public DeserializedToLogicalConverter getDeserializedToLogicalSelectConverter() {
        return new SelectDeserializedToLogicalConverter();
    }

    @Bean(name = DESERIALIZED_TO_LOGICAL_PROJECT_CONVERTER)
    public ProjectDeserializedToLogicalConverter getDeserializedToLogicalProjectConverter() {
        return new ProjectDeserializedToLogicalConverter();
    }

    @Bean(name = DESERIALIZED_TO_LOGICAL_JOIN_CONVERTER)
    public DeserializedToLogicalConverter getDeserializedToLogicalJoinConverter() {
        return new JoinDeserializedToLogicalConverter();
    }

    @Bean(name = DESERIALIZED_TO_LOGICAL_GROUP_BY_CONVERTER)
    public DeserializedToLogicalConverter getDeserializedToLogicalGroupByConverter() {
        return new GroupByDeserializedToLogicalConverter();
    }

    @Bean(name = DESERIALIZED_TO_LOGICAL_AGGREGATE_CONVERTER)
    public DeserializedToLogicalConverter getDeserializedToLogicalAggregateConverter() {
        return new AggregateDeserializedToLogicalConverter();
    }

    @Bean(name = DESERIALIZED_TO_LOGICAL_UNION_CONVERTER)
    public DeserializedToLogicalConverter getDeserializedToLogicalUnionConverter() {
        return new UnionDeserializedToLogicalConverter();
    }

    @Bean
    public DispatchersFactory getDispatchersFactory() {
        return new DispatchersFactory(getFilterInDeserializedLoadDispatcher(),
                getUnaryInputTagStreamDispatcher(),
                getBinaryStreamDispatcher());
    }

    @Bean
    public Dispatcher<DeserializedLoad, DeserializedLoad> getFilterInDeserializedLoadDispatcher() {
        return new FilterInDeserializedLoadDispatcher();
    }

    @Bean
    public Dispatcher<UnaryDeserializedOperation, Stream<String>> getUnaryInputTagStreamDispatcher() {
        return new UnaryInputTagStreamDispatcher();
    }

    @Bean
    public Dispatcher<BinaryDeserializedOperation, Stream<String>> getBinaryStreamDispatcher() {
        return new BinaryInputTagStreamDispatcher();
    }

    @Bean
    public LogicalToPhysicalManager getLogicalOperationsConverter() {
        return new LogicalToPhysicalManager(getProfileDeserializer(), getLogicalConvertersMapping());
    }

    @Bean
    public Deserializer<Profile> getProfileDeserializer() {
        return new AvroProfileDeserializer();
    }

    @Bean(name = LOGICAL_CONVERTERS_MAPPING)
    public Map<String, LogicalToPhysicalConverter> getLogicalConvertersMapping() {
        return Map.of(
                LOGICAL_LOAD, getLogicalToPhysicalLoadConverter(),
                LOGICAL_SELECT, getLogicalToPhysicalSelectConverter(),
                LOGICAL_PROJECT, getLogicalToPhysicalProjectConverter(),
                LOGICAL_JOIN, getLogicalToPhysicalJoinConverter(),
                LOGICAL_GROUP_BY, getLogicalToPhysicalGroupByConverter(),
                LOGICAL_AGGREGATE, getLogicalToPhysicalAggregateConverter(),
                LOGICAL_UNION, getLogicalToPhysicalUnionConverter()
        );
    }

    @Bean(name = LOGICAL_TO_PHYSICAL_LOAD_CONVERTER)
    public LogicalToPhysicalConverter getLogicalToPhysicalLoadConverter() {
        return new LoadLogicalToPhysicalConverter();
    }

    @Bean(name = LOGICAL_TO_PHYSICAL_SELECT_CONVERTER)
    public LogicalToPhysicalConverter getLogicalToPhysicalSelectConverter() {
        return new SelectLogicalToPhysicalConverter(getColumnReader());
    }

    @Bean(name = LOGICAL_TO_PHYSICAL_PROJECT_CONVERTER)
    public ProjectLogicalToPhysicalConverter getLogicalToPhysicalProjectConverter() {
        return new ProjectLogicalToPhysicalConverter();
    }

    @Bean(name = LOGICAL_TO_PHYSICAL_JOIN_CONVERTER)
    public LogicalToPhysicalConverter getLogicalToPhysicalJoinConverter() {
        return new JoinLogicalToPhysicalConverter();
    }

    @Bean(name = LOGICAL_TO_PHYSICAL_GROUP_BY_CONVERTER)
    public LogicalToPhysicalConverter getLogicalToPhysicalGroupByConverter() {
        return new GroupByLogicalToPhysicalConverter();
    }

    @Bean(name = LOGICAL_TO_PHYSICAL_AGGREGATE_CONVERTER)
    public LogicalToPhysicalConverter getLogicalToPhysicalAggregateConverter() {
        return new AggregateLogicalToPhysicalConverter();
    }

    @Bean(name = LOGICAL_TO_PHYSICAL_UNION_CONVERTER)
    public LogicalToPhysicalConverter getLogicalToPhysicalUnionConverter() {
        return new UnionLogicalToPhysicalConverter();
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
                PHYSICAL_AGGREGATE, getInstrumentInvokable(getSparkAggregateInvokable()),
                PHYSICAL_UNION, getInstrumentInvokable(getSparkUnionInvokable()),
                PHYSICAL_SINK, getInstrumentInvokable(getSparkSinkInvokable())
        );
    }

    public Invokable getInstrumentInvokable(Invokable wrappedInvokable) {
        return new InstrumentInvokable(wrappedInvokable, getOperationInstrumentations());
    }

    @Bean(name = SPARK_LOAD_INVOKABLE)
    public Invokable getSparkLoadInvokable() {
        return new LoadSparkInvokable(getSparkSession());
    }

    @Bean(name = SPARK_SELECT_INVOKABLE)
    public Invokable getSparkSelectInvokable() {
        return new SelectSparkInvokable();
    }

    @Bean(name = SPARK_PROJECT_INVOKABLE)
    public Invokable getSparkProjectInvokable() {
        return new ProjectSparkInvokable();
    }

    @Bean(name = SPARK_JOIN_INVOKABLE)
    public Invokable getSparkJoinInvokable() {
        return new JoinSparkInvokable();
    }

    @Bean(name = SPARK_GROUP_BY_INVOKABLE)
    public Invokable getSparkGroupByInvokable() {
        return new GroupBySparkInvokable();
    }

    @Bean(name = SPARK_AGGREGATE_INVOKABLE)
    public Invokable getSparkAggregateInvokable() {
        return new AggregateSparkInvokable();
    }

    @Bean(name = SPARK_UNION_INVOKABLE)
    public Invokable getSparkUnionInvokable() {
        return new UnionSparkInvokable();
    }

    @Bean(name = SPARK_SINK_INVOKABLE)
    public Invokable getSparkSinkInvokable() {
        return new SinkSparkInvokable();
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
                PHYSICAL_AGGREGATE, getSparkAggregateInvokable(),
                PHYSICAL_UNION, getSparkUnionInvokable(),
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
