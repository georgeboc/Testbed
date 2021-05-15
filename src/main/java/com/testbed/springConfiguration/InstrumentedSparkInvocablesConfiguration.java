package com.testbed.springConfiguration;

import com.testbed.boundary.invocations.Invokable;
import com.testbed.boundary.invocations.instrumentation.OperationInstrumenter;
import com.testbed.boundary.invocations.intermediateDatasets.instrumentation.IntermediateDatasetInstrumentation;
import com.testbed.boundary.invocations.intermediateDatasets.instrumentation.SparkIntermediateDatasetInstrumentation;
import com.testbed.boundary.invocations.frameworks.spark.AggregateSparkOperation;
import com.testbed.boundary.invocations.frameworks.spark.GroupbySparkOperation;
import com.testbed.boundary.invocations.frameworks.spark.JoinSparkOperation;
import com.testbed.boundary.invocations.frameworks.spark.LoadSparkOperation;
import com.testbed.boundary.invocations.frameworks.spark.ProjectSparkOperation;
import com.testbed.boundary.invocations.frameworks.spark.SelectSparkOperation;
import com.testbed.boundary.invocations.frameworks.spark.SinkSparkOperation;
import com.testbed.boundary.invocations.frameworks.spark.UnionSparkOperation;
import com.testbed.interactors.monitors.MonitorComposer;
import com.testbed.interactors.monitors.NoMonitor;
import org.apache.hadoop.fs.FileSystem;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.inject.Inject;

import java.util.Collections;

import static com.testbed.springConfiguration.FrameworksConfigurationsConstants.INSTRUMENTED_SPARK;
import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_AGGREGATE;
import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_GROUP_BY;
import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_JOIN;
import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_LOAD;
import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_PROJECT;
import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_SELECT;
import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_SINK;
import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_UNION;

@Configuration
@Profile(INSTRUMENTED_SPARK)
public class InstrumentedSparkInvocablesConfiguration {
    private static final String APP_NAME = "Testbed";
    private static final String COMPRESSION_CODEC_CONFIG = "spark.sql.parquet.compression.codec";
    private static final String NONE = "none";

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
    public Invokable sparkSinkInvokable(FileSystem fileSystem) {
        return beanFactory.getBean(OperationInstrumenter.class, new SinkSparkOperation(fileSystem));
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
                .config(COMPRESSION_CODEC_CONFIG, NONE)
                .getOrCreate();
    }

    @Bean
    public MonitorComposer monitorComposer(NoMonitor noMonitor) {
        return new MonitorComposer(Collections.singletonList(noMonitor));
    }
}
