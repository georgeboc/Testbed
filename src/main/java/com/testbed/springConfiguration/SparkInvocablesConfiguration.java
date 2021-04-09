package com.testbed.springConfiguration;

import com.testbed.boundary.invocations.InstrumentInvokable;
import com.testbed.boundary.invocations.Invokable;
import com.testbed.boundary.invocations.OperationInstrumentation;
import com.testbed.boundary.invocations.spark.AggregateSparkInvokable;
import com.testbed.boundary.invocations.spark.GroupbySparkInvokable;
import com.testbed.boundary.invocations.spark.JoinSparkInvokable;
import com.testbed.boundary.invocations.spark.LoadSparkInvokable;
import com.testbed.boundary.invocations.spark.ProjectSparkInvokable;
import com.testbed.boundary.invocations.spark.SelectSparkInvokable;
import com.testbed.boundary.invocations.spark.SinkSparkInvokable;
import com.testbed.boundary.invocations.spark.UnionSparkInvokable;
import org.apache.spark.sql.SparkSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.inject.Inject;
import java.util.List;

@Configuration
public class SparkInvocablesConfiguration {
    private static final String APP_NAME = "Testbed";
    private static final String LOCAL = "local[*]";

    private static final String PHYSICAL_LOAD = "PhysicalLoad";
    private static final String PHYSICAL_SELECT = "PhysicalSelect";
    private static final String PHYSICAL_PROJECT = "PhysicalProject";
    private static final String PHYSICAL_JOIN = "PhysicalJoin";
    private static final String PHYSICAL_GROUP_BY = "PhysicalGroupby";
    private static final String PHYSICAL_AGGREGATE = "PhysicalAggregate";
    private static final String PHYSICAL_UNION = "PhysicalUnion";
    private static final String PHYSICAL_SINK = "PhysicalSink";

    @Inject
    private List<OperationInstrumentation> operationInstrumentations;

    @Bean(name = PHYSICAL_LOAD)
    public Invokable sparkLoadInvokable() {
        return instrumentInvokable(new LoadSparkInvokable(sparkSession()));
    }

    @Bean(name = PHYSICAL_SELECT)
    public Invokable sparkSelectInvokable() {
        return instrumentInvokable(new SelectSparkInvokable());
    }

    @Bean(name = PHYSICAL_PROJECT)
    public Invokable sparkProjectInvokable() {
        return instrumentInvokable(new ProjectSparkInvokable());
    }

    @Bean(name = PHYSICAL_JOIN)
    public Invokable sparkJoinInvokable() {
        return instrumentInvokable(new JoinSparkInvokable());
    }

    @Bean(name = PHYSICAL_GROUP_BY)
    public Invokable sparkGroupByInvokable() {
        return instrumentInvokable(new GroupbySparkInvokable());
    }

    @Bean(name = PHYSICAL_AGGREGATE)
    public Invokable sparkAggregateInvokable() {
        return instrumentInvokable(new AggregateSparkInvokable());
    }

    @Bean(name = PHYSICAL_UNION)
    public Invokable sparkUnionInvokable() {
        return instrumentInvokable(new UnionSparkInvokable());
    }

    @Bean(name = PHYSICAL_SINK)
    public Invokable sparkSinkInvokable() {
        return instrumentInvokable(new SinkSparkInvokable());
    }

    public Invokable instrumentInvokable(Invokable wrappedInvokable) {
        return new InstrumentInvokable(wrappedInvokable, operationInstrumentations);
    }

    @Bean
    public SparkSession sparkSession() {
        return SparkSession.builder()
                .appName(APP_NAME)
                .master(LOCAL)
                .getOrCreate();
    }
}
