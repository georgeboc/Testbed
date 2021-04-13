package com.testbed.springConfiguration;

import com.testbed.boundary.invocations.Invokable;
import com.testbed.boundary.invocations.OperationInstrumentation;
import com.testbed.boundary.invocations.intermediateDatasets.instrumentation.SparkIntermediateDatasetInstrumentation;
import com.testbed.boundary.invocations.spark.AggregateSparkOperation;
import com.testbed.boundary.invocations.spark.GroupbySparkOperation;
import com.testbed.boundary.invocations.spark.JoinSparkOperation;
import com.testbed.boundary.invocations.spark.LoadSparkOperation;
import com.testbed.boundary.invocations.spark.ProjectSparkOperation;
import com.testbed.boundary.invocations.spark.SelectSparkOperation;
import com.testbed.boundary.invocations.spark.SinkSparkOperation;
import com.testbed.boundary.invocations.spark.UnionSparkOperation;
import org.apache.spark.sql.SparkSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static com.testbed.springConfiguration.InvocablesConfigurationCommons.PHYSICAL_AGGREGATE;
import static com.testbed.springConfiguration.InvocablesConfigurationCommons.PHYSICAL_GROUP_BY;
import static com.testbed.springConfiguration.InvocablesConfigurationCommons.PHYSICAL_JOIN;
import static com.testbed.springConfiguration.InvocablesConfigurationCommons.PHYSICAL_LOAD;
import static com.testbed.springConfiguration.InvocablesConfigurationCommons.PHYSICAL_PROJECT;
import static com.testbed.springConfiguration.InvocablesConfigurationCommons.PHYSICAL_SELECT;
import static com.testbed.springConfiguration.InvocablesConfigurationCommons.PHYSICAL_SINK;
import static com.testbed.springConfiguration.InvocablesConfigurationCommons.PHYSICAL_UNION;
import static com.testbed.springConfiguration.InvocablesConfigurationCommons.instrumentInvokable;

@Configuration
public class SparkInvocablesConfiguration {
    private static final String APP_NAME = "Testbed";
    private static final String LOCAL = "local[*]";

    @Bean(name = PHYSICAL_LOAD)
    public Invokable sparkLoadInvokable(SparkIntermediateDatasetInstrumentation sparkIntermediateDatasetInstrumentation,
                                        List<OperationInstrumentation> operationInstrumentations) {
        return instrumentInvokable(new LoadSparkOperation(sparkSession()),
                sparkIntermediateDatasetInstrumentation,
                operationInstrumentations);
    }

    @Bean(name = PHYSICAL_SELECT)
    public Invokable sparkSelectInvokable(SparkIntermediateDatasetInstrumentation sparkIntermediateDatasetInstrumentation,
                                          List<OperationInstrumentation> operationInstrumentations) {
        return instrumentInvokable(new SelectSparkOperation(),
                sparkIntermediateDatasetInstrumentation,
                operationInstrumentations);
    }

    @Bean(name = PHYSICAL_PROJECT)
    public Invokable sparkProjectInvokable(SparkIntermediateDatasetInstrumentation sparkIntermediateDatasetInstrumentation,
                                           List<OperationInstrumentation> operationInstrumentations) {
        return instrumentInvokable(new ProjectSparkOperation(),
                sparkIntermediateDatasetInstrumentation,
                operationInstrumentations);
    }

    @Bean(name = PHYSICAL_JOIN)
    public Invokable sparkJoinInvokable(SparkIntermediateDatasetInstrumentation sparkIntermediateDatasetInstrumentation,
                                        List<OperationInstrumentation> operationInstrumentations) {
        return instrumentInvokable(new JoinSparkOperation(),
                sparkIntermediateDatasetInstrumentation,
                operationInstrumentations);
    }

    @Bean(name = PHYSICAL_GROUP_BY)
    public Invokable sparkGroupByInvokable(SparkIntermediateDatasetInstrumentation sparkIntermediateDatasetInstrumentation,
                                           List<OperationInstrumentation> operationInstrumentations) {
        return instrumentInvokable(new GroupbySparkOperation(),
                sparkIntermediateDatasetInstrumentation,
                operationInstrumentations);
    }

    @Bean(name = PHYSICAL_AGGREGATE)
    public Invokable sparkAggregateInvokable(SparkIntermediateDatasetInstrumentation sparkIntermediateDatasetInstrumentation,
                                             List<OperationInstrumentation> operationInstrumentations) {
        return instrumentInvokable(new AggregateSparkOperation(),
                sparkIntermediateDatasetInstrumentation,
                operationInstrumentations);
    }

    @Bean(name = PHYSICAL_UNION)
    public Invokable sparkUnionInvokable(SparkIntermediateDatasetInstrumentation sparkIntermediateDatasetInstrumentation,
                                         List<OperationInstrumentation> operationInstrumentations) {
        return instrumentInvokable(new UnionSparkOperation(),
                sparkIntermediateDatasetInstrumentation,
                operationInstrumentations);
    }

    @Bean(name = PHYSICAL_SINK)
    public Invokable sparkSinkInvokable(SparkIntermediateDatasetInstrumentation sparkIntermediateDatasetInstrumentation,
                                        List<OperationInstrumentation> operationInstrumentations) {
        return instrumentInvokable(new SinkSparkOperation(sparkIntermediateDatasetInstrumentation),
                sparkIntermediateDatasetInstrumentation,
                operationInstrumentations);
    }

    @Bean
    public SparkIntermediateDatasetInstrumentation sparkIntermediateDatasetInstrumentation() {
        return new SparkIntermediateDatasetInstrumentation();
    }

    @Bean
    public SparkSession sparkSession() {
        return SparkSession.builder()
                .appName(APP_NAME)
                .master(LOCAL)
                .getOrCreate();
    }
}
