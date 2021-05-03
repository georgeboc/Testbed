package com.testbed.springConfiguration;

import com.testbed.boundary.invocations.Invokable;
import com.testbed.boundary.invocations.frameworks.mapReduce.groupBy.GroupByMapReduceOperation;
import com.testbed.boundary.invocations.frameworks.mapReduce.JobConfigurationCommons;
import com.testbed.boundary.invocations.frameworks.mapReduce.join.JoinMapReduceOperation;
import com.testbed.boundary.invocations.frameworks.mapReduce.load.LoadMapReduceOperation;
import com.testbed.boundary.invocations.frameworks.mapReduce.project.ProjectMapReduceOperation;
import com.testbed.boundary.invocations.frameworks.mapReduce.select.SelectMapReduceOperation;
import com.testbed.boundary.invocations.frameworks.mapReduce.sink.SinkMapReduceOperation;
import com.testbed.boundary.invocations.frameworks.mapReduce.sumAggregator.SumAggregateMapReduceOperation;
import com.testbed.boundary.invocations.frameworks.mapReduce.union.UnionMapReduceOperation;
import com.testbed.boundary.utils.DirectoryUtils;
import com.testbed.boundary.utils.ParquetSchemaReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import static com.testbed.springConfiguration.FrameworksConfigurationsConstants.TIMED_MAPREDUCE;
import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_AGGREGATE;
import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_GROUP_BY;
import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_JOIN;
import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_LOAD;
import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_PROJECT;
import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_SELECT;
import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_SINK;
import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_UNION;


@Configuration
@Profile(TIMED_MAPREDUCE)
public class TimedMapReduceInvocablesConfiguration {

    @Bean
    @Qualifier(PHYSICAL_LOAD)
    public Invokable mapReduceLoadInvokable() {
        return new LoadMapReduceOperation();
    }

    @Bean
    @Qualifier(PHYSICAL_SELECT)
    public Invokable mapReduceSelectInvokable(JobConfigurationCommons jobConfigurationCommons,
                                              ParquetSchemaReader parquetSchemaReader) {
        return new SelectMapReduceOperation(jobConfigurationCommons, parquetSchemaReader);
    }

    @Bean
    @Qualifier(PHYSICAL_PROJECT)
    public Invokable mapReduceProjectInvokable(JobConfigurationCommons jobConfigurationCommons,
                                               ParquetSchemaReader parquetSchemaReader) {
        return new ProjectMapReduceOperation(jobConfigurationCommons, parquetSchemaReader);
    }

    @Bean
    @Qualifier(PHYSICAL_GROUP_BY)
    public Invokable mapReduceGroupByInvokable(JobConfigurationCommons jobConfigurationCommons,
                                               ParquetSchemaReader parquetSchemaReader) {
        return new GroupByMapReduceOperation(jobConfigurationCommons, parquetSchemaReader);
    }

    @Bean
    @Qualifier(PHYSICAL_AGGREGATE)
    public Invokable mapReduceAggregateInvokable(JobConfigurationCommons jobConfigurationCommons) {
        return new SumAggregateMapReduceOperation(jobConfigurationCommons);
    }

    @Bean
    @Qualifier(PHYSICAL_JOIN)
    public Invokable mapReduceJoinInvokable(JobConfigurationCommons jobConfigurationCommons,
                                            ParquetSchemaReader parquetSchemaReader) {
        return new JoinMapReduceOperation(jobConfigurationCommons, parquetSchemaReader);
    }

    @Bean
    @Qualifier(PHYSICAL_UNION)
    public Invokable mapReduceUnionInvokable(JobConfigurationCommons jobConfigurationCommons,
                                             ParquetSchemaReader parquetSchemaReader) {
        return new UnionMapReduceOperation(jobConfigurationCommons, parquetSchemaReader);
    }

    @Bean
    @Qualifier(PHYSICAL_SINK)
    public Invokable mapReduceSinkInvokable() {
        return new SinkMapReduceOperation();
    }

    @Bean
    public ParquetSchemaReader parquetSchemaReader(org.apache.hadoop.conf.Configuration configuration, DirectoryUtils directoryUtils) {
        return new ParquetSchemaReader(configuration, directoryUtils);
    }

    @Bean
    public JobConfigurationCommons jobConfigurationCommons(org.apache.hadoop.conf.Configuration configuration) {
        return new JobConfigurationCommons(configuration);
    }
}
