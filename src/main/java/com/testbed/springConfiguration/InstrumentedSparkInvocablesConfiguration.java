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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.inject.Inject;

import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_AGGREGATE;
import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_GROUP_BY;
import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_JOIN;
import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_LOAD;
import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_PROJECT;
import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_SELECT;
import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_SINK;
import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_UNION;

@Configuration
public class InstrumentedSparkInvocablesConfiguration {
    private static final String APP_NAME = "Testbed";

    @Inject
    private BeanFactory beanFactory;

    @Bean
    @Qualifier(PHYSICAL_LOAD)
    public Invokable sparkLoadInvokable(SparkSession sparkSession) {
        return beanFactory.getBean(OperationInstrumenter.class, new LoadSparkOperation(sparkSession));
    }

    @Bean
    @Qualifier(PHYSICAL_SELECT)
    public Invokable sparkSelectInvokable() {
        return beanFactory.getBean(OperationInstrumenter.class, new SelectSparkOperation());
    }

    @Bean
    @Qualifier(PHYSICAL_PROJECT)
    public Invokable sparkProjectInvokable() {
        return beanFactory.getBean(OperationInstrumenter.class, new ProjectSparkOperation());
    }

    @Bean
    @Qualifier(PHYSICAL_JOIN)
    public Invokable sparkJoinInvokable() {
        return beanFactory.getBean(OperationInstrumenter.class, new JoinSparkOperation());
    }

    @Bean
    @Qualifier(PHYSICAL_GROUP_BY)
    public Invokable sparkGroupByInvokable() {
        return beanFactory.getBean(OperationInstrumenter.class, new GroupbySparkOperation());
    }

    @Bean
    @Qualifier(PHYSICAL_AGGREGATE)
    public Invokable sparkAggregateInvokable() {
        return beanFactory.getBean(OperationInstrumenter.class, new AggregateSparkOperation());
    }

    @Bean
    @Qualifier(PHYSICAL_UNION)
    public Invokable sparkUnionInvokable() {
        return beanFactory.getBean(OperationInstrumenter.class, new UnionSparkOperation());
    }

    @Bean
    @Qualifier(PHYSICAL_SINK)
    public Invokable sparkSinkInvokable() {
        return beanFactory.getBean(OperationInstrumenter.class, new SinkSparkOperation());
    }

    @Bean
    public IntermediateDatasetInstrumentation intermediateDatasetInstrumentation() {
        return new SparkIntermediateDatasetInstrumentation();
    }

    @Bean
    public SparkSession sparkSession(@Value("${clusterMode.spark}") String sparkClusterMode) {
        return SparkSession.builder()
                .appName(APP_NAME)
                .master(sparkClusterMode)
                .getOrCreate();
    }
}
