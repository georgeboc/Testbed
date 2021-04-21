package com.testbed.springConfiguration;

import com.testbed.boundary.invocations.Invokable;
import com.testbed.boundary.invocations.instrumentation.OperationInstrumenter;
import com.testbed.boundary.invocations.intermediateDatasets.instrumentation.IntermediateDatasetInstrumentation;
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
import org.springframework.beans.factory.BeanFactory;
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

public class InstrumentedSparkInvocablesConfiguration {
    private static final String APP_NAME = "Testbed";
    private static final String LOCAL = "local[*]";

    @Inject
    private BeanFactory beanFactory;

    @Bean(name = PHYSICAL_LOAD)
    public Invokable sparkLoadInvokable(SparkSession sparkSession) {
        return beanFactory.getBean(OperationInstrumenter.class, new LoadSparkOperation(sparkSession));
    }

    @Bean(name = PHYSICAL_SELECT)
    public Invokable sparkSelectInvokable() {
        return beanFactory.getBean(OperationInstrumenter.class, new SelectSparkOperation());
    }

    @Bean(name = PHYSICAL_PROJECT)
    public Invokable sparkProjectInvokable() {
        return beanFactory.getBean(OperationInstrumenter.class, new ProjectSparkOperation());
    }

    @Bean(name = PHYSICAL_JOIN)
    public Invokable sparkJoinInvokable() {
        return beanFactory.getBean(OperationInstrumenter.class, new JoinSparkOperation());
    }

    @Bean(name = PHYSICAL_GROUP_BY)
    public Invokable sparkGroupByInvokable() {
        return beanFactory.getBean(OperationInstrumenter.class, new GroupbySparkOperation());
    }

    @Bean(name = PHYSICAL_AGGREGATE)
    public Invokable sparkAggregateInvokable() {
        return beanFactory.getBean(OperationInstrumenter.class, new AggregateSparkOperation());
    }

    @Bean(name = PHYSICAL_UNION)
    public Invokable sparkUnionInvokable() {
        return beanFactory.getBean(OperationInstrumenter.class, new UnionSparkOperation());
    }

    @Bean(name = PHYSICAL_SINK)
    public Invokable sparkSinkInvokable(IntermediateDatasetInstrumentation intermediateDatasetInstrumentation) {
        return beanFactory.getBean(OperationInstrumenter.class, new SinkSparkOperation(intermediateDatasetInstrumentation));
    }

    @Bean
    public IntermediateDatasetInstrumentation intermediateDatasetInstrumentation() {
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
