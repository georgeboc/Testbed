package com.testbed.springConfiguration;

import com.testbed.boundary.invocations.Invokable;
import com.testbed.boundary.invocations.instrumentation.OperationInstrumentation;
import com.testbed.boundary.invocations.intermediateDatasets.instrumentation.CountMapReduce;
import com.testbed.boundary.invocations.intermediateDatasets.instrumentation.MapReduceIntermediateDatasetInstrumentation;
import com.testbed.boundary.invocations.mapReduce.JobConfigurationCommons;
import com.testbed.boundary.invocations.mapReduce.LoadMapReduceOperation;
import com.testbed.boundary.invocations.mapReduce.SelectMapReduceOperation;
import com.testbed.boundary.invocations.mapReduce.SinkDebugMapReduceOperation;
import com.testbed.boundary.utils.ParquetSchemaReader;
import org.apache.hadoop.conf.Configuration;
import org.springframework.context.annotation.Bean;

import java.util.List;

import static com.testbed.springConfiguration.OperationsConfigurationCommons.PHYSICAL_LOAD;
import static com.testbed.springConfiguration.OperationsConfigurationCommons.PHYSICAL_SELECT;
import static com.testbed.springConfiguration.OperationsConfigurationCommons.PHYSICAL_SINK;
import static com.testbed.springConfiguration.OperationsConfigurationCommons.checkSelectOperationTolerableError;
import static com.testbed.springConfiguration.OperationsConfigurationCommons.instrumentOperation;

public class MapReduceInvocablesConfiguration {
    @Bean(name = PHYSICAL_LOAD)
    public Invokable mapReduceLoadInvokable(List<OperationInstrumentation> operationInstrumentations,
                                            MapReduceIntermediateDatasetInstrumentation mapReduceIntermediateDatasetInstrumentation) {
        return instrumentOperation(new LoadMapReduceOperation(), mapReduceIntermediateDatasetInstrumentation, operationInstrumentations);
    }

    @Bean(name = PHYSICAL_SELECT)
    public Invokable mapReduceSelectInvokable(JobConfigurationCommons jobConfigurationCommons,
                                              ParquetSchemaReader parquetSchemaReader,
                                              MapReduceIntermediateDatasetInstrumentation mapReduceIntermediateDatasetInstrumentation,
                                              List<OperationInstrumentation> operationInstrumentations) {
        return checkSelectOperationTolerableError(
                instrumentOperation(new SelectMapReduceOperation(jobConfigurationCommons, parquetSchemaReader),
                        mapReduceIntermediateDatasetInstrumentation,
                        operationInstrumentations),
                mapReduceIntermediateDatasetInstrumentation);
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
