package com.testbed.springConfiguration;

import com.testbed.boundary.invocations.Invokable;
import com.testbed.boundary.invocations.intermediateDatasets.instrumentation.IntermediateDatasetInstrumentation;
import com.testbed.boundary.invocations.spark.AggregateSparkOperation;
import com.testbed.boundary.invocations.spark.GroupbySparkOperation;
import com.testbed.boundary.invocations.spark.JoinSparkOperation;
import com.testbed.boundary.invocations.spark.LoadSparkOperation;
import com.testbed.boundary.invocations.spark.ProjectSparkOperation;
import com.testbed.boundary.invocations.spark.SelectSparkOperation;
import com.testbed.boundary.invocations.spark.SinkSparkOperation;
import com.testbed.boundary.invocations.spark.UnionSparkOperation;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;

import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_AGGREGATE;
import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_GROUP_BY;
import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_JOIN;
import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_LOAD;
import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_PROJECT;
import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_SELECT;
import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_SINK;
import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_UNION;

public class SparkInvocablesConfiguration {
    private static final String APP_NAME = "Testbed";
    private static final String LOCAL = "local[*]";

    @Bean
    @Qualifier(PHYSICAL_LOAD)
    public Invokable sparkLoadInvokable(SparkSession sparkSession) {
        return new LoadSparkOperation(sparkSession);
    }

    @Bean
    @Qualifier(PHYSICAL_SELECT)
    public Invokable sparkSelectInvokable() {
        return new SelectSparkOperation();
    }

    @Bean
    @Qualifier(PHYSICAL_PROJECT)
    public Invokable sparkProjectInvokable() {
        return new ProjectSparkOperation();
    }

    @Bean
    @Qualifier(PHYSICAL_JOIN)
    public Invokable sparkJoinInvokable() {
        return new JoinSparkOperation();
    }

    @Bean
    @Qualifier(PHYSICAL_GROUP_BY)
    public Invokable sparkGroupByInvokable() {
        return new GroupbySparkOperation();
    }

    @Bean
    @Qualifier(PHYSICAL_AGGREGATE)
    public Invokable sparkAggregateInvokable() {
        return new AggregateSparkOperation();
    }

    @Bean
    @Qualifier(PHYSICAL_UNION)
    public Invokable sparkUnionInvokable() {
        return new UnionSparkOperation();
    }

    @Bean
    @Qualifier(PHYSICAL_SINK)
    public Invokable sparkSinkInvokable() {
        return new SinkSparkOperation();
    }

    @Bean
    public SparkSession sparkSession() {
        return SparkSession.builder()
                .appName(APP_NAME)
                .master(LOCAL)
                .getOrCreate();
    }
}
