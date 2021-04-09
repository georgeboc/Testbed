package com.testbed.springConfiguration;

import com.testbed.boundary.deserializers.Deserializer;
import com.testbed.boundary.readers.ColumnReader;
import com.testbed.entities.profiles.Profile;
import com.testbed.interactors.converters.logicalToPhysical.AggregateLogicalToPhysicalConverter;
import com.testbed.interactors.converters.logicalToPhysical.GroupByLogicalToPhysicalConverter;
import com.testbed.interactors.converters.logicalToPhysical.JoinLogicalToPhysicalConverter;
import com.testbed.interactors.converters.logicalToPhysical.LoadLogicalToPhysicalConverter;
import com.testbed.interactors.converters.logicalToPhysical.LogicalToPhysicalConverter;
import com.testbed.interactors.converters.logicalToPhysical.LogicalToPhysicalManager;
import com.testbed.interactors.converters.logicalToPhysical.ProjectLogicalToPhysicalConverter;
import com.testbed.interactors.converters.logicalToPhysical.SelectLogicalToPhysicalConverter;
import com.testbed.interactors.converters.logicalToPhysical.UnionLogicalToPhysicalConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogicalToPhysicalConfiguration {
    public static final String LOGICAL_LOAD = "LogicalLoad";
    public static final String LOGICAL_SELECT = "LogicalSelect";
    public static final String LOGICAL_PROJECT = "LogicalProject";
    public static final String LOGICAL_JOIN = "LogicalJoin";
    public static final String LOGICAL_GROUP_BY = "LogicalGroupby";
    public static final String LOGICAL_AGGREGATE = "LogicalAggregate";
    public static final String LOGICAL_UNION = "LogicalUnion";

    @Bean
    public LogicalToPhysicalManager logicalToPhysicalManager(Deserializer<Profile> profileDeserializer) {
        return new LogicalToPhysicalManager(profileDeserializer);
    }

    @Bean
    @Qualifier(LOGICAL_LOAD)
    public LogicalToPhysicalConverter loadLogicalToPhysicalConverter() {
        return new LoadLogicalToPhysicalConverter();
    }

    @Bean
    @Qualifier(LOGICAL_SELECT)
    public LogicalToPhysicalConverter selectLogicalToPhysicalConverter(ColumnReader columnReader) {
        return new SelectLogicalToPhysicalConverter(columnReader);
    }

    @Bean
    @Qualifier(LOGICAL_PROJECT)
    public LogicalToPhysicalConverter projectLogicalToPhysicalConverter() {
        return new ProjectLogicalToPhysicalConverter();
    }

    @Bean
    @Qualifier(LOGICAL_JOIN)
    public LogicalToPhysicalConverter joinLogicalToPhysicalConverter() {
        return new JoinLogicalToPhysicalConverter();
    }

    @Bean
    @Qualifier(LOGICAL_GROUP_BY)
    public LogicalToPhysicalConverter groupByLogicalToPhysicalConverter() {
        return new GroupByLogicalToPhysicalConverter();
    }

    @Bean
    @Qualifier(LOGICAL_AGGREGATE)
    public LogicalToPhysicalConverter aggregateLogicalToPhysicalConverter() {
        return new AggregateLogicalToPhysicalConverter();
    }

    @Bean
    @Qualifier(LOGICAL_UNION)
    public LogicalToPhysicalConverter unionLogicalToPhysicalConverter() {
        return new UnionLogicalToPhysicalConverter();
    }
}
