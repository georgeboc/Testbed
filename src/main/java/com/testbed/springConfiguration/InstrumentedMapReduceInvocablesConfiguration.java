package com.testbed.springConfiguration;

import com.testbed.boundary.invocations.Invokable;
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
import com.testbed.boundary.utils.DirectoryUtils;
import com.testbed.boundary.utils.ParquetSchemaReader;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;

import javax.inject.Inject;

import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_AGGREGATE;
import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_GROUP_BY;
import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_JOIN;
import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_LOAD;
import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_PROJECT;
import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_SELECT;
import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_SINK;
import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_UNION;


public class InstrumentedMapReduceInvocablesConfiguration {
    @Inject
    private BeanFactory beanFactory;

    @Bean
    @Qualifier(PHYSICAL_LOAD)
    public Invokable mapReduceLoadInvokable() {
        return beanFactory.getBean(OperationInstrumenter.class, new LoadMapReduceOperation());
    }

    @Bean
    @Qualifier(PHYSICAL_SELECT)
    public Invokable mapReduceSelectInvokable(JobConfigurationCommons jobConfigurationCommons,
                                              ParquetSchemaReader parquetSchemaReader) {
        return beanFactory.getBean(OperationInstrumenter.class, new SelectMapReduceOperation(jobConfigurationCommons,
                parquetSchemaReader));
    }

    @Bean
    @Qualifier(PHYSICAL_PROJECT)
    public Invokable mapReduceProjectInvokable(JobConfigurationCommons jobConfigurationCommons,
                                               ParquetSchemaReader parquetSchemaReader) {
        return beanFactory.getBean(OperationInstrumenter.class, new ProjectMapReduceOperation(jobConfigurationCommons,
                parquetSchemaReader));
    }

    @Bean
    @Qualifier(PHYSICAL_GROUP_BY)
    public Invokable mapReduceGroupByInvokable(JobConfigurationCommons jobConfigurationCommons,
                                               ParquetSchemaReader parquetSchemaReader) {
        return beanFactory.getBean(OperationInstrumenter.class, new GroupByMapReduceOperation(jobConfigurationCommons,
                parquetSchemaReader));
    }

    @Bean
    @Qualifier(PHYSICAL_AGGREGATE)
    public Invokable mapReduceAggregateInvokable(JobConfigurationCommons jobConfigurationCommons) {
        return beanFactory.getBean(OperationInstrumenter.class, new SumAggregateMapReduceOperation(jobConfigurationCommons));
    }

    @Bean
    @Qualifier(PHYSICAL_JOIN)
    public Invokable mapReduceJoinInvokable(JobConfigurationCommons jobConfigurationCommons,
                                            ParquetSchemaReader parquetSchemaReader) {
        return beanFactory.getBean(OperationInstrumenter.class, new JoinMapReduceOperation(jobConfigurationCommons,
                parquetSchemaReader));
    }

    @Bean
    @Qualifier(PHYSICAL_UNION)
    public Invokable mapReduceUnionInvokable(JobConfigurationCommons jobConfigurationCommons,
                                             ParquetSchemaReader parquetSchemaReader) {
        return beanFactory.getBean(OperationInstrumenter.class, new UnionMapReduceOperation(jobConfigurationCommons,
                parquetSchemaReader));
    }

    @Bean
    @Qualifier(PHYSICAL_SINK)
    public Invokable mapReduceSinkInvokable(JobConfigurationCommons jobConfigurationCommons) {
        return beanFactory.getBean(OperationInstrumenter.class, new SinkDebugMapReduceOperation(jobConfigurationCommons));
    }

    @Bean
    public IntermediateDatasetInstrumentation intermediateDatasetInstrumentation(CountMapReduce countMapReduce,
                                                                                 ParquetSchemaReader parquetSchemaReader) {
        return new MapReduceIntermediateDatasetInstrumentation(countMapReduce, parquetSchemaReader);
    }

    @Bean
    public CountMapReduce countMapReduce(DirectoryUtils directoryUtils,
                                         FileSystem fileSystem,
                                         JobConfigurationCommons jobConfigurationCommons) {
        return new CountMapReduce(directoryUtils, fileSystem, jobConfigurationCommons);
    }

    @Bean
    public ParquetSchemaReader parquetSchemaReader(Configuration configuration, DirectoryUtils directoryUtils) {
        return new ParquetSchemaReader(configuration, directoryUtils);
    }

    @Bean
    public JobConfigurationCommons jobConfigurationCommons(Configuration configuration) {
        return new JobConfigurationCommons(configuration);
    }
}
