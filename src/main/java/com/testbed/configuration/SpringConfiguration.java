package com.testbed.configuration;

import com.clearspring.analytics.util.Lists;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.testbed.boundary.deserializers.AvroProfileDeserializer;
import com.testbed.boundary.deserializers.Deserializer;
import com.testbed.boundary.deserializers.JsonOperationsDeserializer;
import com.testbed.boundary.executors.Executable;
import com.testbed.boundary.executors.InstrumentedExecutable;
import com.testbed.boundary.executors.spark.LoadExecutable;
import com.testbed.boundary.executors.spark.SelectExecutable;
import com.testbed.boundary.executors.spark.SinkExecutable;
import com.testbed.boundary.readers.AvroColumnReader;
import com.testbed.boundary.readers.ColumnReader;
import com.testbed.boundary.serializers.CSVOperationInstrumentationsSerializer;
import com.testbed.boundary.serializers.Serializer;
import com.testbed.entities.instrumentation.OperationInstrumentation;
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
import com.testbed.interactors.executors.Executor;
import com.testbed.interactors.executors.TopologicalSorter;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.sql.SparkSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
public class SpringConfiguration {
    private static final String DESERIALIZED_SELECT_CONVERTER = "deserializedSelectConverter";
    private static final String DESERIALIZED_LOAD_CONVERTER = "deserializedLoadConverter";

    private static final String LOGICAL_SELECT_CONVERTER = "logicalSelectConverter";
    private static final String LOGICAL_LOAD_CONVERTER = "logicalLoadConverter";

    private static final String DESERIALIZED_CONVERTERS_MAPPING = "deserializedConvertersMapping";
    private static final String LOGICAL_CONVERTERS_MAPPING = "logicalConvertersMapping";
    private static final String SPARK_PHYSICAL_OPERATION_TO_EXECUTABLE_MAPPING = "sparkPhysicalOperationToExecutableMapper";
    private static final String SPARK_PHYSICAL_OPERATION_TO_INSTRUMENTED_EXECUTABLE_MAPPING = "sparkPhysicalOperationToInstrumentedExecutableMapper";


    private static final String DESERIALIZED_SELECT = "DeserializedSelect";
    private static final String DESERIALIZED_LOAD = "DeserializedLoad";

    private static final String LOGICAL_SELECT = "LogicalSelect";
    private static final String LOGICAL_LOAD = "LogicalLoad";

    private static final String PHYSICAL_LOAD = "PhysicalLoad";
    private static final String PHYSICAL_SELECT = "PhysicalSelect";
    private static final String PHYSICAL_SINK = "PhysicalSink";

    private static final String SPARK_LOAD_EXECUTABLE = "sparkLoadExecutable";
    private static final String SPARK_SELECT_EXECUTABLE = "sparkSelectExecutable";
    private static final String SPARK_SINK_EXECUTABLE = "sparkSinkExecutable";

    private static final String APP_NAME = "Testbed";
    private static final String LOCAL = "local[*]";

    @Bean
    public Serializer<List<OperationInstrumentation>> getCallInstrumentationsSerializer() {
        return new CSVOperationInstrumentationsSerializer();
    }

    @Bean
    public Deserializer<DeserializedOperations> getOperationsDeserializer() {
        return new JsonOperationsDeserializer();
    }

    @Bean
    public Deserializer<Profile> getProfileDeserializer() {
        return new AvroProfileDeserializer();
    }

    @Bean
    public ColumnReader getColumnReader() {
        return new AvroColumnReader();
    }

    @Bean
    public InteractorFactory getReadJsonAndPrintContentFactory() {
        return new InteractorFactory(getOperationsDeserializer(),
                getDeserializedOperationsConverter(),
                getLogicalOperationsConverter(),
                getSparkExecutor(),
                getCallInstrumentations(),
                getCallInstrumentationsSerializer());
    }

    @Bean(name=DESERIALIZED_LOAD_CONVERTER)
    public DeserializedToLogicalOperationConverter getDeserializedLoadConverter() {
        return new DeserializedToLogicalLoadConverter();
    }

    @Bean(name=DESERIALIZED_SELECT_CONVERTER)
    public DeserializedToLogicalOperationConverter getDeserializedSelectConverter() {
        return new DeserializedToLogicalSelectConverter();
    }

    @Bean(name=DESERIALIZED_CONVERTERS_MAPPING)
    public Map<String, DeserializedToLogicalOperationConverter> getDeserializedConvertersMapping() {
        return Maps.newHashMap(ImmutableMap.of(
                DESERIALIZED_LOAD, getDeserializedLoadConverter(),
                DESERIALIZED_SELECT, getDeserializedSelectConverter()
        ));
    }

    @Bean
    public DeserializedToLogicalOperationsConverter getDeserializedOperationsConverter() {
        return new DeserializedToLogicalOperationsConverter(getDeserializedConvertersMapping());
    }

    @Bean(name=LOGICAL_LOAD_CONVERTER)
    public LogicalToPhysicalOperationConverter getLogicalLoadConverter() {
        return new LogicalToPhysicalLoadConverter();
    }

    @Bean(name=LOGICAL_SELECT_CONVERTER)
    public LogicalToPhysicalOperationConverter getLogicalSelectConverter() {
        return new LogicalToPhysicalSelectConverter(getColumnReader());
    }

    @Bean(name=LOGICAL_CONVERTERS_MAPPING)
    public Map<String, LogicalToPhysicalOperationConverter> getLogicalConvertersMapping() {
        return Maps.newHashMap(ImmutableMap.of(
                LOGICAL_LOAD, getLogicalLoadConverter(),
                LOGICAL_SELECT, getLogicalSelectConverter()
        ));
    }

    @Bean
    public LogicalToPhysicalOperationsConverter getLogicalOperationsConverter() {
         return new LogicalToPhysicalOperationsConverter(getProfileDeserializer(), getLogicalConvertersMapping());
    }

    @Bean
    public TopologicalSorter getTopologicalSorter() {
        return new TopologicalSorter();
    }

    @Bean
    public List<OperationInstrumentation> getCallInstrumentations() {
        return Lists.newArrayList();
    }

    public Executable getInstrumentedExecutable(Executable wrappedExecutable) {
        return new InstrumentedExecutable(wrappedExecutable, getCallInstrumentations());
    }

    @Bean
    public SparkSession getSparkSession() {
        SparkConf conf = new SparkConf().setAppName(APP_NAME).setMaster(LOCAL);
        SparkContext sparkContext = new SparkContext(conf);
        return new SparkSession(sparkContext);
    }

    @Bean(name=SPARK_LOAD_EXECUTABLE)
    public Executable getSparkLoadExecutable() {
        return new LoadExecutable(getSparkSession());
    }

    @Bean(name=SPARK_SELECT_EXECUTABLE)
    public Executable getSparkSelectExecutable() {
        return new SelectExecutable();
    }

    @Bean(name=SPARK_SINK_EXECUTABLE)
    public Executable getSparkSinkExecutable() {
        return new SinkExecutable();
    }

    @Bean(name=SPARK_PHYSICAL_OPERATION_TO_EXECUTABLE_MAPPING)
    public Map<String, Executable> getSparkPhysicalOperationToExecutableMapper() {
        return Maps.newHashMap(ImmutableMap.of(
                PHYSICAL_LOAD, getSparkLoadExecutable(),
                PHYSICAL_SELECT, getSparkSelectExecutable(),
                PHYSICAL_SINK, getSparkSinkExecutable()
        ));
    }

    @Bean(name=SPARK_PHYSICAL_OPERATION_TO_INSTRUMENTED_EXECUTABLE_MAPPING)
    public Map<String, Executable> getSparkPhysicalOperationToInstrumentedExecutableMapper() {
        return Maps.newHashMap(ImmutableMap.of(
                PHYSICAL_LOAD, getInstrumentedExecutable(getSparkLoadExecutable()),
                PHYSICAL_SELECT, getInstrumentedExecutable(getSparkSelectExecutable()),
                PHYSICAL_SINK, getInstrumentedExecutable(getSparkSinkExecutable())
        ));
    }

    @Bean
    public Executor getSparkExecutor() {
        return new Executor(getTopologicalSorter(), getSparkPhysicalOperationToInstrumentedExecutableMapper());
    }
}
