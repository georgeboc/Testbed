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
import com.testbed.interactors.converters.deserializedToLogical.DeserializedToLogicalConverter;
import com.testbed.interactors.converters.deserializedToLogical.DeserializedToLogicalManager;
import com.testbed.interactors.converters.deserializedToLogical.GroupByDeserializedToLogicalConverter;
import com.testbed.interactors.converters.deserializedToLogical.JoinDeserializedToLogicalConverter;
import com.testbed.interactors.converters.deserializedToLogical.LoadDeserializedToLogicalConverter;
import com.testbed.interactors.converters.deserializedToLogical.ProjectDeserializedToLogicalConverter;
import com.testbed.interactors.converters.deserializedToLogical.SelectDeserializedToLogicalConverter;
import com.testbed.interactors.converters.deserializedToLogical.UnionDeserializedToLogicalConverter;
import com.testbed.interactors.converters.logicalToPhysical.AggregateLogicalToPhysicalConverter;
import com.testbed.interactors.converters.logicalToPhysical.GroupByLogicalToPhysicalConverter;
import com.testbed.interactors.converters.logicalToPhysical.JoinLogicalToPhysicalConverter;
import com.testbed.interactors.converters.logicalToPhysical.LoadLogicalToPhysicalConverter;
import com.testbed.interactors.converters.logicalToPhysical.LogicalToPhysicalConverter;
import com.testbed.interactors.converters.logicalToPhysical.LogicalToPhysicalManager;
import com.testbed.interactors.converters.logicalToPhysical.ProjectLogicalToPhysicalConverter;
import com.testbed.interactors.converters.logicalToPhysical.SelectLogicalToPhysicalConverter;
import com.testbed.interactors.converters.logicalToPhysical.UnionLogicalToPhysicalConverter;
import com.testbed.interactors.dispatchers.Dispatcher;
import com.testbed.interactors.dispatchers.DispatchersFactory;
import com.testbed.interactors.dispatchers.filterIn.FilterInDeserializedLoadDispatcher;
import com.testbed.interactors.dispatchers.inputTagStream.BinaryInputTagStreamDispatcher;
import com.testbed.interactors.dispatchers.inputTagStream.UnaryInputTagStreamDispatcher;
import com.testbed.interactors.jobs.JobCreator;
import com.testbed.interactors.jobs.JobInvoker;
import com.testbed.interactors.validators.semantic.InputsCountValidatorManager;
import com.testbed.interactors.validators.syntactic.NotNullOnAllFieldsValidator;
import com.testbed.interactors.validators.syntactic.NotNullOnAllFieldsValidatorManager;
import com.testbed.interactors.viewers.InvocationInstrumentationViewer;
import com.testbed.views.InvocationInstrumentationView;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.sql.SparkSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Stream;

@Configuration
public class SpringConfiguration {
    private static final String APP_NAME = "Testbed";
    private static final String LOCAL = "local[*]";
    private static final String OBJECT_MAPPER_WITH_DESERIALIZED_OPERATION_MIXIN = "objectMapperWithDeserializedOperationMixin";
    private static final String OBJECT_MAPPER_WITH_JAVA_TIME_MODULE = "objectMapperWithJavaTimeModule";

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

    @Bean
    public ColumnReader getColumnReader() {
        return new AvroColumnReader();
    }

    @Bean
    public InteractorFactory interactorFactory() {
        return new InteractorFactory(operationsDeserializer(),
                notNullOnAllFieldsValidatorManager(),
                deserializedToLogicalOperationsConverter(),
                inputsCountValidatorManager(),
                logicalOperationsConverter(),
                jobCreator(),
                jobInvoker(),
                operationInstrumentations(),
                invocationInstrumentationViewer());
    }

    @Bean
    public Deserializer<DeserializedOperations> operationsDeserializer() {
        return new JsonOperationsDeserializer(objectMapperWithDeserializedOperationMixin());
    }

    @Bean
    public NotNullOnAllFieldsValidatorManager notNullOnAllFieldsValidatorManager() {
        return new NotNullOnAllFieldsValidatorManager(notNullOnAllFieldsValidator());
    }

    @Bean
    public NotNullOnAllFieldsValidator notNullOnAllFieldsValidator() {
        return new NotNullOnAllFieldsValidator();
    }

    @Bean(name = OBJECT_MAPPER_WITH_DESERIALIZED_OPERATION_MIXIN)
    public ObjectMapper objectMapperWithDeserializedOperationMixin() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.addMixIn(DeserializedOperation.class, DeserializedOperationMixin.class);
        return objectMapper;
    }

    @Bean
    public DeserializedToLogicalManager deserializedToLogicalOperationsConverter() {
        return new DeserializedToLogicalManager(dispatchersFactory());
    }

    @Bean(name = DESERIALIZED_LOAD)
    public DeserializedToLogicalConverter loadDeserializedToLogicalConverter() {
        return new LoadDeserializedToLogicalConverter();
    }

    @Bean(name = DESERIALIZED_SELECT)
    public DeserializedToLogicalConverter selectDeserializedToLogicalConverter() {
        return new SelectDeserializedToLogicalConverter();
    }

    @Bean(name = DESERIALIZED_PROJECT)
    public ProjectDeserializedToLogicalConverter projectDeserializedToLogicalConverter() {
        return new ProjectDeserializedToLogicalConverter();
    }

    @Bean(name = DESERIALIZED_JOIN)
    public DeserializedToLogicalConverter joinDeserializedToLogicalConverter() {
        return new JoinDeserializedToLogicalConverter();
    }

    @Bean(name = DESERIALIZED_GROUP_BY)
    public DeserializedToLogicalConverter groupByDeserializedToLogicalConverter() {
        return new GroupByDeserializedToLogicalConverter();
    }

    @Bean(name = DESERIALIZED_AGGREGATE)
    public DeserializedToLogicalConverter aggregateDeserializedToLogicalConverter() {
        return new AggregateDeserializedToLogicalConverter();
    }

    @Bean(name = DESERIALIZED_UNION)
    public DeserializedToLogicalConverter unionDeserializedToLogicalConverter() {
        return new UnionDeserializedToLogicalConverter();
    }

    @Bean
    public InputsCountValidatorManager inputsCountValidatorManager() {
        return new InputsCountValidatorManager();
    }

    @Bean
    public DispatchersFactory dispatchersFactory() {
        return new DispatchersFactory(filterInDeserializedLoadDispatcher(),
                unaryInputTagStreamDispatcher(),
                binaryStreamDispatcher());
    }

    @Bean
    public Dispatcher<DeserializedLoad, DeserializedLoad> filterInDeserializedLoadDispatcher() {
        return new FilterInDeserializedLoadDispatcher();
    }

    @Bean
    public Dispatcher<UnaryDeserializedOperation, Stream<String>> unaryInputTagStreamDispatcher() {
        return new UnaryInputTagStreamDispatcher();
    }

    @Bean
    public Dispatcher<BinaryDeserializedOperation, Stream<String>> binaryStreamDispatcher() {
        return new BinaryInputTagStreamDispatcher();
    }

    @Bean
    public LogicalToPhysicalManager logicalOperationsConverter() {
        return new LogicalToPhysicalManager(profileDeserializer());
    }

    @Bean
    public Deserializer<Profile> profileDeserializer() {
        return new AvroProfileDeserializer();
    }

    @Bean(name = LOGICAL_LOAD)
    public LogicalToPhysicalConverter loadLogicalToPhysicalConverter() {
        return new LoadLogicalToPhysicalConverter();
    }

    @Bean(name = LOGICAL_SELECT)
    public LogicalToPhysicalConverter selectLogicalToPhysicalConverter() {
        return new SelectLogicalToPhysicalConverter(getColumnReader());
    }

    @Bean(name = LOGICAL_PROJECT)
    public ProjectLogicalToPhysicalConverter projectLogicalToPhysicalConverter() {
        return new ProjectLogicalToPhysicalConverter();
    }

    @Bean(name = LOGICAL_JOIN)
    public LogicalToPhysicalConverter joinLogicalToPhysicalConverter() {
        return new JoinLogicalToPhysicalConverter();
    }

    @Bean(name = LOGICAL_GROUP_BY)
    public LogicalToPhysicalConverter groupByLogicalToPhysicalConverter() {
        return new GroupByLogicalToPhysicalConverter();
    }

    @Bean(name = LOGICAL_AGGREGATE)
    public LogicalToPhysicalConverter aggregateLogicalToPhysicalConverter() {
        return new AggregateLogicalToPhysicalConverter();
    }

    @Bean(name = LOGICAL_UNION)
    public LogicalToPhysicalConverter unionLogicalToPhysicalConverter() {
        return new UnionLogicalToPhysicalConverter();
    }

    @Bean
    public JobCreator jobCreator() {
        return new JobCreator();
    }

    @Bean
    public JobInvoker jobInvoker() {
        return new JobInvoker();
    }

    @Bean(name = PHYSICAL_LOAD)
    public Invokable sparkLoadInvokable() {
        return instrumentInvokable(new LoadSparkInvokable(sparkSession()));
    }

    @Bean(name = PHYSICAL_SELECT)
    public Invokable sparkSelectInvokable() {
        return instrumentInvokable(new SelectSparkInvokable());
    }

    @Bean(name = PHYSICAL_PROJECT)
    public Invokable sparkProjectInvokable() {
        return instrumentInvokable(new ProjectSparkInvokable());
    }

    @Bean(name = PHYSICAL_JOIN)
    public Invokable sparkJoinInvokable() {
        return instrumentInvokable(new JoinSparkInvokable());
    }

    @Bean(name = PHYSICAL_GROUP_BY)
    public Invokable sparkGroupByInvokable() {
        return instrumentInvokable(new GroupBySparkInvokable());
    }

    @Bean(name = PHYSICAL_AGGREGATE)
    public Invokable sparkAggregateInvokable() {
        return instrumentInvokable(new AggregateSparkInvokable());
    }

    @Bean(name = PHYSICAL_UNION)
    public Invokable sparkUnionInvokable() {
        return instrumentInvokable(new UnionSparkInvokable());
    }

    @Bean(name = PHYSICAL_SINK)
    public Invokable sparkSinkInvokable() {
        return instrumentInvokable(new SinkSparkInvokable());
    }

    public Invokable instrumentInvokable(Invokable wrappedInvokable) {
        return new InstrumentInvokable(wrappedInvokable, operationInstrumentations());
    }

    @Bean
    public List<OperationInstrumentation> operationInstrumentations() {
        return Lists.newArrayList();
    }

    @Bean
    public SparkSession sparkSession() {
        SparkConf conf = new SparkConf().setAppName(APP_NAME).setMaster(LOCAL);
        SparkContext sparkContext = new SparkContext(conf);
        return new SparkSession(sparkContext);
    }

    @Bean
    public InvocationInstrumentationViewer invocationInstrumentationViewer() {
        return new InvocationInstrumentationViewer(invocationInstrumentationViewCSVSerializer(), objectMapperWithJavaTimeModule());
    }

    @Bean(name = OBJECT_MAPPER_WITH_JAVA_TIME_MODULE)
    public ObjectMapper objectMapperWithJavaTimeModule() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    @Bean
    public CSVSerializer<InvocationInstrumentationView> invocationInstrumentationViewCSVSerializer() {
        return new CSVSerializer<>(InvocationInstrumentationView.class);
    }
}
