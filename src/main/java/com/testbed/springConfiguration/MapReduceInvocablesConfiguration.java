package com.testbed.springConfiguration;

import com.testbed.boundary.invocations.Invokable;
import com.testbed.boundary.invocations.mapReduce.GroupByMapReduceOperation;
import com.testbed.boundary.invocations.mapReduce.JobConfigurationCommons;
import com.testbed.boundary.invocations.mapReduce.JoinMapReduceOperation;
import com.testbed.boundary.invocations.mapReduce.LoadMapReduceOperation;
import com.testbed.boundary.invocations.mapReduce.ProjectMapReduceOperation;
import com.testbed.boundary.invocations.mapReduce.SelectMapReduceOperation;
import com.testbed.boundary.invocations.mapReduce.SinkMapReduceOperation;
import com.testbed.boundary.invocations.mapReduce.SumAggregateMapReduceOperation;
import com.testbed.boundary.invocations.mapReduce.UnionMapReduceOperation;
import com.testbed.boundary.utils.ParquetSchemaReader;
import org.apache.hadoop.conf.Configuration;
import org.springframework.context.annotation.Bean;

import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_AGGREGATE;
import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_GROUP_BY;
import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_JOIN;
import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_LOAD;
import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_PROJECT;
import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_SELECT;
import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_SINK;
import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_UNION;


public class MapReduceInvocablesConfiguration {

    @Bean(name = PHYSICAL_LOAD)
    public Invokable mapReduceLoadInvokable() {
        return new LoadMapReduceOperation();
    }

    @Bean(name = PHYSICAL_SELECT)
    public Invokable mapReduceSelectInvokable(JobConfigurationCommons jobConfigurationCommons,
                                              ParquetSchemaReader parquetSchemaReader) {
        return new SelectMapReduceOperation(jobConfigurationCommons, parquetSchemaReader);
    }

    @Bean(name = PHYSICAL_PROJECT)
    public Invokable mapReduceProjectInvokable(JobConfigurationCommons jobConfigurationCommons,
                                               ParquetSchemaReader parquetSchemaReader) {
        return new ProjectMapReduceOperation(jobConfigurationCommons, parquetSchemaReader);
    }

    @Bean(name = PHYSICAL_GROUP_BY)
    public Invokable mapReduceGroupByInvokable(JobConfigurationCommons jobConfigurationCommons,
                                               ParquetSchemaReader parquetSchemaReader) {
        return new GroupByMapReduceOperation(jobConfigurationCommons, parquetSchemaReader);
    }

    @Bean(name = PHYSICAL_AGGREGATE)
    public Invokable mapReduceAggregateInvokable(JobConfigurationCommons jobConfigurationCommons) {
        return new SumAggregateMapReduceOperation(jobConfigurationCommons);
    }

    @Bean(name = PHYSICAL_JOIN)
    public Invokable mapReduceJoinInvokable(JobConfigurationCommons jobConfigurationCommons,
                                            ParquetSchemaReader parquetSchemaReader) {
        return new JoinMapReduceOperation(jobConfigurationCommons, parquetSchemaReader);
    }

    @Bean(name = PHYSICAL_UNION)
    public Invokable mapReduceUnionInvokable(JobConfigurationCommons jobConfigurationCommons,
                                             ParquetSchemaReader parquetSchemaReader) {
        return new UnionMapReduceOperation(jobConfigurationCommons, parquetSchemaReader);
    }

    @Bean(name = PHYSICAL_SINK)
    public Invokable mapReduceSinkInvokable() {
        return new SinkMapReduceOperation();
    }

    @Bean
    public ParquetSchemaReader parquetSchemaReader(Configuration configuration) {
        return new ParquetSchemaReader(configuration);
    }

    @Bean
    public Configuration configuration() {
        return new Configuration();
    }

    @Bean
    public JobConfigurationCommons jobConfigurationCommons(Configuration configuration) {
        return new JobConfigurationCommons(configuration);
    }
}
