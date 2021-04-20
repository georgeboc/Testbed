package com.testbed.springConfiguration;

import com.testbed.boundary.invocations.Invokable;
import com.testbed.boundary.invocations.Operation;
import com.testbed.boundary.invocations.instrumentation.OperationInstrumenter;
import com.testbed.boundary.invocations.intermediateDatasets.instrumentation.CountMapReduce;
import com.testbed.boundary.invocations.intermediateDatasets.instrumentation.IntermediateDatasetInstrumentation;
import com.testbed.boundary.invocations.intermediateDatasets.instrumentation.MapReduceIntermediateDatasetInstrumentation;
import com.testbed.boundary.invocations.mapReduce.GroupByMapReduceOperation;
import com.testbed.boundary.invocations.mapReduce.JobConfigurationCommons;
import com.testbed.boundary.invocations.mapReduce.JoinMapReduceOperation;
import com.testbed.boundary.invocations.mapReduce.LoadMapReduceOperation;
import com.testbed.boundary.invocations.mapReduce.ProjectMapReduceOperation;
import com.testbed.boundary.invocations.mapReduce.SelectMapReduceOperation;
import com.testbed.boundary.invocations.mapReduce.SinkDebugMapReduceOperation;
import com.testbed.boundary.invocations.mapReduce.SumAggregateMapReduceOperation;
import com.testbed.boundary.invocations.mapReduce.UnionMapReduceOperation;
import com.testbed.boundary.utils.ParquetSchemaReader;
import org.apache.hadoop.conf.Configuration;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.Bean;

import javax.inject.Inject;

import static com.testbed.springConfiguration.OperationsConfigurationCommons.PHYSICAL_AGGREGATE;
import static com.testbed.springConfiguration.OperationsConfigurationCommons.PHYSICAL_GROUP_BY;
import static com.testbed.springConfiguration.OperationsConfigurationCommons.PHYSICAL_JOIN;
import static com.testbed.springConfiguration.OperationsConfigurationCommons.PHYSICAL_LOAD;
import static com.testbed.springConfiguration.OperationsConfigurationCommons.PHYSICAL_PROJECT;
import static com.testbed.springConfiguration.OperationsConfigurationCommons.PHYSICAL_SELECT;
import static com.testbed.springConfiguration.OperationsConfigurationCommons.PHYSICAL_SINK;
import static com.testbed.springConfiguration.OperationsConfigurationCommons.PHYSICAL_UNION;


public class MapReduceInvocablesConfiguration {
    @Inject
    private BeanFactory beanFactory;

    @Bean(name = PHYSICAL_LOAD)
    public Invokable mapReduceLoadInvokable() {
        return beanFactory.getBean(OperationInstrumenter.class, new LoadMapReduceOperation());
    }

    @Bean(name = PHYSICAL_SELECT)
    public Invokable mapReduceSelectInvokable(JobConfigurationCommons jobConfigurationCommons,
                                              ParquetSchemaReader parquetSchemaReader) {
        return beanFactory.getBean(OperationInstrumenter.class, new SelectMapReduceOperation(jobConfigurationCommons, parquetSchemaReader));
    }

    @Bean(name = PHYSICAL_PROJECT)
    public Invokable mapReduceProjectInvokable(JobConfigurationCommons jobConfigurationCommons,
                                               ParquetSchemaReader parquetSchemaReader) {
        return beanFactory.getBean(OperationInstrumenter.class, new ProjectMapReduceOperation(jobConfigurationCommons, parquetSchemaReader));
    }

    @Bean(name = PHYSICAL_GROUP_BY)
    public Invokable mapReduceGroupByInvokable(JobConfigurationCommons jobConfigurationCommons,
                                               ParquetSchemaReader parquetSchemaReader) {
        return beanFactory.getBean(OperationInstrumenter.class, new GroupByMapReduceOperation(jobConfigurationCommons, parquetSchemaReader));
    }

    @Bean(name = PHYSICAL_AGGREGATE)
    public Invokable mapReduceAggregateInvokable(JobConfigurationCommons jobConfigurationCommons) {
        return beanFactory.getBean(OperationInstrumenter.class, new SumAggregateMapReduceOperation(jobConfigurationCommons));
    }

    @Bean(name = PHYSICAL_JOIN)
    public Invokable mapReduceJoinInvokable(JobConfigurationCommons jobConfigurationCommons,
                                            ParquetSchemaReader parquetSchemaReader) {
        return beanFactory.getBean(OperationInstrumenter.class, new JoinMapReduceOperation(jobConfigurationCommons, parquetSchemaReader));
    }

    @Bean(name = PHYSICAL_UNION)
    public Invokable mapReduceUnionInvokable(JobConfigurationCommons jobConfigurationCommons,
                                             ParquetSchemaReader parquetSchemaReader) {
        return beanFactory.getBean(OperationInstrumenter.class, new UnionMapReduceOperation(jobConfigurationCommons, parquetSchemaReader));
    }

    @Bean(name = PHYSICAL_SINK)
    public Invokable mapReduceSinkInvokable(JobConfigurationCommons jobConfigurationCommons) {
        return beanFactory.getBean(OperationInstrumenter.class, new SinkDebugMapReduceOperation(jobConfigurationCommons));
    }

    @Bean
    public IntermediateDatasetInstrumentation intermediateDatasetInstrumentation(CountMapReduce countMapReduce,
                                                                                 ParquetSchemaReader parquetSchemaReader) {
        return new MapReduceIntermediateDatasetInstrumentation(countMapReduce, parquetSchemaReader);
    }

    @Bean
    public CountMapReduce countMapReduce(JobConfigurationCommons jobConfigurationCommons) {
        return new CountMapReduce(jobConfigurationCommons);
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
