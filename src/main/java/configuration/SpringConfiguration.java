package configuration;

import boundary.deserializers.DatasetProfileDeserializer;
import boundary.deserializers.JsonDatasetProfileDeserializer;
import boundary.deserializers.JsonOperationsDeserializer;
import boundary.deserializers.OperationsDeserializer;
import boundary.readers.FileReader;
import boundary.readers.Reader;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import factories.InteractorFactory;
import interactors.converters.deserializedToLogical.DeserializedLoadConverter;
import interactors.converters.deserializedToLogical.DeserializedOperationConverter;
import interactors.converters.deserializedToLogical.DeserializedOperationsConverter;
import interactors.converters.deserializedToLogical.DeserializedSelectConverter;
import interactors.converters.logicalToPhysical.LogicalLoadConverter;
import interactors.converters.logicalToPhysical.LogicalOperationConverter;
import interactors.converters.logicalToPhysical.LogicalOperationsConverter;
import interactors.converters.logicalToPhysical.LogicalSelectConverter;
import interactors.converters.logicalToPhysical.LogicalSinkConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class SpringConfiguration {
    private static final String DESERIALIZED_SELECT_CONVERTER = "deserializedSelectConverter";
    private static final String LOGICAL_SELECT_CONVERTER = "logicalSelectConverter";
    private static final String DESERIALIZED_LOAD_CONVERTER = "deserializedLoadConverter";
    private static final String LOGICAL_LOAD_CONVERTER = "logicalLoadConverter";
    private static final String LOGICAL_SINK_CONVERTER = "logicalSinkConverter";

    private static final String DESERIALIZED_CONVERTERS_MAPPING = "deserializedConvertersMapping";
    private static final String LOGICAL_CONVERTERS_MAPPING = "logicalConvertersMapping";

    private static final String DESERIALIZED_SELECT = "DeserializedSelect";
    private static final String LOGICAL_SELECT = "LogicalSelect";
    private static final String DESERIALIZED_LOAD = "DeserializedLoad";
    private static final String LOGICAL_LOAD = "LogicalLoad";
    private static final String LOGICAL_SINK = "LogicalSink";

    @Bean
    public OperationsDeserializer getPipelineDeserializer() {
        return new JsonOperationsDeserializer();
    }

    @Bean
    public DatasetProfileDeserializer getDatasetProfileDeserializer() {
        return new JsonDatasetProfileDeserializer();
    }

    @Bean
    public InteractorFactory getReadJsonAndPrintContentFactory() {
        return new InteractorFactory(getPipelineDeserializer(), getDeserializedOperationsConverter(), getReader());
    }

    @Bean(name=DESERIALIZED_LOAD_CONVERTER)
    public DeserializedOperationConverter getDeserializedLoadConverter() {
        return new DeserializedLoadConverter();
    }

    @Bean(name=DESERIALIZED_SELECT_CONVERTER)
    public DeserializedOperationConverter getDeserializedSelectConverter() {
        return new DeserializedSelectConverter();
    }

    @Bean(name=DESERIALIZED_CONVERTERS_MAPPING)
    public Map<String, DeserializedOperationConverter> getDeserializedConvertersMapping() {
        return Maps.newHashMap(ImmutableMap.of(
                DESERIALIZED_LOAD, getDeserializedLoadConverter(),
                DESERIALIZED_SELECT, getDeserializedSelectConverter()
        ));
    }

    @Bean
    public DeserializedOperationsConverter getDeserializedOperationsConverter() {
        return new DeserializedOperationsConverter(getDeserializedConvertersMapping());
    }

    @Bean(name=LOGICAL_LOAD_CONVERTER)
    public LogicalOperationConverter getLogicalLoadConverter() {
        return new LogicalLoadConverter();
    }

    @Bean(name=LOGICAL_SELECT_CONVERTER)
    public LogicalOperationConverter getLogicalSelectConverter() {
        return new LogicalSelectConverter();
    }

    @Bean(name=LOGICAL_SINK_CONVERTER)
    public LogicalOperationConverter getLogicalSinkConverter() {
        return new LogicalSinkConverter();
    }

    @Bean(name=LOGICAL_CONVERTERS_MAPPING)
    public Map<String, LogicalOperationConverter> getLogicalConvertersMapping() {
        return Maps.newHashMap(ImmutableMap.of(
                LOGICAL_LOAD, getLogicalLoadConverter(),
                LOGICAL_SELECT, getLogicalSelectConverter(),
                LOGICAL_SINK, getLogicalSinkConverter()
        ));
    }

    @Bean
    public LogicalOperationsConverter getLogicalOperationsConverter() {
         return new LogicalOperationsConverter(getReader(), getDatasetProfileDeserializer(), getLogicalConvertersMapping());
    }

    @Bean
    public Reader getReader() {
        return new FileReader();
    }
}
