package com.testbed.springConfiguration;

import com.testbed.boundary.invocations.Invokable;
import com.testbed.boundary.invocations.instrumentation.OperationInstrumentation;
import com.testbed.boundary.invocations.intermediateDatasets.instrumentation.CountMapReduce;
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
import org.springframework.context.annotation.Bean;

import java.util.List;

import static com.testbed.springConfiguration.OperationsConfigurationCommons.PHYSICAL_AGGREGATE;
import static com.testbed.springConfiguration.OperationsConfigurationCommons.PHYSICAL_GROUP_BY;
import static com.testbed.springConfiguration.OperationsConfigurationCommons.PHYSICAL_JOIN;
import static com.testbed.springConfiguration.OperationsConfigurationCommons.PHYSICAL_LOAD;
import static com.testbed.springConfiguration.OperationsConfigurationCommons.PHYSICAL_PROJECT;
import static com.testbed.springConfiguration.OperationsConfigurationCommons.PHYSICAL_SELECT;
import static com.testbed.springConfiguration.OperationsConfigurationCommons.PHYSICAL_SINK;
import static com.testbed.springConfiguration.OperationsConfigurationCommons.PHYSICAL_UNION;
import static com.testbed.springConfiguration.OperationsConfigurationCommons.instrumentOperation;

public class MapReduceInvocablesConfiguration {
    @Bean(name = PHYSICAL_LOAD)
    public Invokable mapReduceLoadInvokable(List<OperationInstrumentation> operationInstrumentations,
                                            MapReduceIntermediateDatasetInstrumentation mapReduceIntermediateDatasetInstrumentation) {
        return instrumentOperation(new LoadMapReduceOperation(),
                mapReduceIntermediateDatasetInstrumentation,
                operationInstrumentations);
    }

    @Bean(name = PHYSICAL_SELECT)
    public Invokable mapReduceSelectInvokable(JobConfigurationCommons jobConfigurationCommons,
                                              ParquetSchemaReader parquetSchemaReader,
                                              MapReduceIntermediateDatasetInstrumentation mapReduceIntermediateDatasetInstrumentation,
                                              List<OperationInstrumentation> operationInstrumentations) {
        return instrumentOperation(new SelectMapReduceOperation(jobConfigurationCommons, parquetSchemaReader),
                        mapReduceIntermediateDatasetInstrumentation,
                        operationInstrumentations);
    }

    @Bean(name = PHYSICAL_PROJECT)
    public Invokable mapReduceProjectInvokable(List<OperationInstrumentation> operationInstrumentations,
                                               JobConfigurationCommons jobConfigurationCommons,
                                               ParquetSchemaReader parquetSchemaReader,
                                               MapReduceIntermediateDatasetInstrumentation mapReduceIntermediateDatasetInstrumentation) {
        return instrumentOperation(new ProjectMapReduceOperation(jobConfigurationCommons, parquetSchemaReader),
                mapReduceIntermediateDatasetInstrumentation, operationInstrumentations);
    }

    @Bean(name = PHYSICAL_GROUP_BY)
    public Invokable mapReduceGroupByInvokable(List<OperationInstrumentation> operationInstrumentations,
                                               JobConfigurationCommons jobConfigurationCommons,
                                               ParquetSchemaReader parquetSchemaReader,
                                               MapReduceIntermediateDatasetInstrumentation mapReduceIntermediateDatasetInstrumentation) {
        return instrumentOperation(new GroupByMapReduceOperation(jobConfigurationCommons, parquetSchemaReader),
                mapReduceIntermediateDatasetInstrumentation, operationInstrumentations);
    }

    @Bean(name = PHYSICAL_AGGREGATE)
    public Invokable mapReduceAggregateInvokable(List<OperationInstrumentation> operationInstrumentations,
                                                 JobConfigurationCommons jobConfigurationCommons,
                                                 MapReduceIntermediateDatasetInstrumentation mapReduceIntermediateDatasetInstrumentation) {
        return instrumentOperation(new SumAggregateMapReduceOperation(jobConfigurationCommons),
                mapReduceIntermediateDatasetInstrumentation, operationInstrumentations);
    }

    @Bean(name = PHYSICAL_JOIN)
    public Invokable mapReduceJoinInvokable(List<OperationInstrumentation> operationInstrumentations,
                                            JobConfigurationCommons jobConfigurationCommons,
                                            ParquetSchemaReader parquetSchemaReader,
                                            MapReduceIntermediateDatasetInstrumentation mapReduceIntermediateDatasetInstrumentation) {
        return instrumentOperation(new JoinMapReduceOperation(jobConfigurationCommons, parquetSchemaReader),
                mapReduceIntermediateDatasetInstrumentation, operationInstrumentations);
    }

    @Bean(name = PHYSICAL_UNION)
    public Invokable mapReduceUnionInvokable(List<OperationInstrumentation> operationInstrumentations,
                                             JobConfigurationCommons jobConfigurationCommons,
                                             ParquetSchemaReader parquetSchemaReader,
                                             MapReduceIntermediateDatasetInstrumentation mapReduceIntermediateDatasetInstrumentation) {
        return instrumentOperation(new UnionMapReduceOperation(jobConfigurationCommons, parquetSchemaReader),
                mapReduceIntermediateDatasetInstrumentation, operationInstrumentations);
    }

    @Bean(name = PHYSICAL_SINK)
    public Invokable mapReduceSinkInvokable(JobConfigurationCommons jobConfigurationCommons,
                                            MapReduceIntermediateDatasetInstrumentation mapReduceIntermediateDatasetInstrumentation,
                                            List<OperationInstrumentation> operationInstrumentations) {
        return instrumentOperation(new SinkDebugMapReduceOperation(jobConfigurationCommons),
                mapReduceIntermediateDatasetInstrumentation,
                operationInstrumentations);
    }

    @Bean
    public MapReduceIntermediateDatasetInstrumentation mapReduceIntermediateDatasetInstrumentation(CountMapReduce countMapReduce,
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
