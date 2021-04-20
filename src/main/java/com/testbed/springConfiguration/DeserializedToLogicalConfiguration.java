package com.testbed.springConfiguration;

import com.testbed.interactors.converters.deserializedToLogical.AggregateDeserializedToLogicalConverter;
import com.testbed.interactors.converters.deserializedToLogical.DeserializedToLogicalConverter;
import com.testbed.interactors.converters.deserializedToLogical.DeserializedToLogicalConverterManager;
import com.testbed.interactors.converters.deserializedToLogical.GroupByDeserializedToLogicalConverter;
import com.testbed.interactors.converters.deserializedToLogical.JoinDeserializedToLogicalConverter;
import com.testbed.interactors.converters.deserializedToLogical.LoadDeserializedToLogicalConverter;
import com.testbed.interactors.converters.deserializedToLogical.ProjectDeserializedToLogicalConverter;
import com.testbed.interactors.converters.deserializedToLogical.SelectDeserializedToLogicalConverter;
import com.testbed.interactors.converters.deserializedToLogical.UnionDeserializedToLogicalConverter;
import com.testbed.interactors.converters.deserializedToLogical.inputTagStream.BinaryInputTagsStream;
import com.testbed.interactors.converters.deserializedToLogical.inputTagStream.InputTagsStream;
import com.testbed.interactors.converters.deserializedToLogical.inputTagStream.UnaryInputTagsStream;
import com.testbed.interactors.converters.deserializedToLogical.inputTagStream.ZeroaryInputTagsStream;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeserializedToLogicalConfiguration {
    public static final String DESERIALIZED_LOAD = "DeserializedLoad";
    public static final String DESERIALIZED_SELECT = "DeserializedSelect";
    public static final String DESERIALIZED_PROJECT = "DeserializedProject";
    public static final String DESERIALIZED_JOIN = "DeserializedJoin";
    public static final String DESERIALIZED_GROUP_BY = "DeserializedGroupBy";
    public static final String DESERIALIZED_AGGREGATE = "DeserializedAggregate";
    public static final String DESERIALIZED_UNION = "DeserializedUnion";

    @Bean
    public DeserializedToLogicalConverterManager deserializedToLogicalOperationsConverter() {
        return new DeserializedToLogicalConverterManager();
    }

    @Bean
    @Qualifier(DESERIALIZED_LOAD)
    public DeserializedToLogicalConverter loadDeserializedToLogicalConverter() {
        return new LoadDeserializedToLogicalConverter();
    }

    @Bean
    @Qualifier(DESERIALIZED_SELECT)
    public DeserializedToLogicalConverter selectDeserializedToLogicalConverter() {
        return new SelectDeserializedToLogicalConverter();
    }

    @Bean
    @Qualifier(DESERIALIZED_PROJECT)
    public DeserializedToLogicalConverter projectDeserializedToLogicalConverter() {
        return new ProjectDeserializedToLogicalConverter();
    }

    @Bean
    @Qualifier(DESERIALIZED_JOIN)
    public DeserializedToLogicalConverter joinDeserializedToLogicalConverter() {
        return new JoinDeserializedToLogicalConverter();
    }

    @Bean
    @Qualifier(DESERIALIZED_GROUP_BY)
    public DeserializedToLogicalConverter groupByDeserializedToLogicalConverter() {
        return new GroupByDeserializedToLogicalConverter();
    }

    @Bean
    @Qualifier(DESERIALIZED_AGGREGATE)
    public DeserializedToLogicalConverter aggregateDeserializedToLogicalConverter() {
        return new AggregateDeserializedToLogicalConverter();
    }

    @Bean
    @Qualifier(DESERIALIZED_UNION)
    public DeserializedToLogicalConverter unionDeserializedToLogicalConverter() {
        return new UnionDeserializedToLogicalConverter();
    }

    @Bean
    @Qualifier(DESERIALIZED_LOAD)
    public InputTagsStream loadInputTagStream() {
        return new ZeroaryInputTagsStream();
    }

    @Bean
    @Qualifier(DESERIALIZED_AGGREGATE)
    public InputTagsStream aggregateInputTagStream() {
        return new UnaryInputTagsStream();
    }

    @Bean
    @Qualifier(DESERIALIZED_GROUP_BY)
    public InputTagsStream groupByInputTagStream() {
        return new UnaryInputTagsStream();
    }

    @Bean
    @Qualifier(DESERIALIZED_PROJECT)
    public InputTagsStream projectInputTagStream() {
        return new UnaryInputTagsStream();
    }

    @Bean
    @Qualifier(DESERIALIZED_SELECT)
    public InputTagsStream selectInputTagStream() {
        return new UnaryInputTagsStream();
    }

    @Bean
    @Qualifier(DESERIALIZED_JOIN)
    public InputTagsStream joinInputTagStream() {
        return new BinaryInputTagsStream();
    }

    @Bean
    @Qualifier(DESERIALIZED_UNION)
    public InputTagsStream unionInputTagStream() {
        return new BinaryInputTagsStream();
    }
}
