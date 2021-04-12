package com.testbed.springConfiguration;

import com.testbed.boundary.invocations.Invokable;
import com.testbed.boundary.invocations.OperationInstrumentation;
import com.testbed.boundary.invocations.mapReduce.JobConfigurationCommons;
import com.testbed.boundary.invocations.mapReduce.LoadMapReduceOperation;
import com.testbed.boundary.invocations.mapReduce.SelectMapReduceOperation;
import com.testbed.boundary.invocations.mapReduce.SinkDebugMapReduceOperation;
import org.apache.hadoop.conf.Configuration;
import org.springframework.context.annotation.Bean;

import java.util.List;

import static com.testbed.springConfiguration.InvocablesConfigurationCommons.PHYSICAL_LOAD;
import static com.testbed.springConfiguration.InvocablesConfigurationCommons.PHYSICAL_SELECT;
import static com.testbed.springConfiguration.InvocablesConfigurationCommons.PHYSICAL_SINK;
import static com.testbed.springConfiguration.InvocablesConfigurationCommons.instrumentInvokable;

public class MapReduceInvocablesConfiguration {
    @Bean(name = PHYSICAL_LOAD)
    public Invokable mapReduceLoadInvokable(List<OperationInstrumentation> operationInstrumentations) {
        return instrumentInvokable(new LoadMapReduceOperation(), operationInstrumentations);
    }

    @Bean(name = PHYSICAL_SELECT)
    public Invokable mapReduceSelectInvokable(JobConfigurationCommons jobConfigurationCommons,
                                              Configuration configuration,
                                              List<OperationInstrumentation> operationInstrumentations) {
        return instrumentInvokable(new SelectMapReduceOperation(jobConfigurationCommons, configuration),
                operationInstrumentations);
    }

    @Bean(name = PHYSICAL_SINK)
    public Invokable mapReduceSinkInvokable(JobConfigurationCommons jobConfigurationCommons,
                                            List<OperationInstrumentation> operationInstrumentations) {
        return instrumentInvokable(new SinkDebugMapReduceOperation(jobConfigurationCommons), operationInstrumentations);
    }

    @Bean
    public JobConfigurationCommons jobConfigurationCommons(Configuration configuration) {
        return new JobConfigurationCommons(configuration);
    }

    @Bean
    public Configuration configuration() {
        return new Configuration();
    }
}
