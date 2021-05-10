package com.testbed.springConfiguration;

import com.testbed.boundary.invocations.Invokable;
import com.testbed.boundary.invocations.frameworks.spark.AggregateSparkOperation;
import com.testbed.boundary.invocations.frameworks.spark.GroupbySparkOperation;
import com.testbed.boundary.invocations.frameworks.spark.JoinSparkOperation;
import com.testbed.boundary.invocations.frameworks.spark.LoadSparkOperation;
import com.testbed.boundary.invocations.frameworks.spark.ProjectSparkOperation;
import com.testbed.boundary.invocations.frameworks.spark.SelectSparkOperation;
import com.testbed.boundary.invocations.frameworks.spark.SinkSparkOperation;
import com.testbed.boundary.invocations.frameworks.spark.UnionSparkOperation;
import com.testbed.interactors.monitors.ChronometerMonitor;
import com.testbed.interactors.monitors.DistributedFileSystemMonitor;
import com.testbed.interactors.monitors.MonitorComposer;
import org.apache.hadoop.fs.FileSystem;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;

import static com.testbed.springConfiguration.FrameworksConfigurationsConstants.TIMED_SPARK;
import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_AGGREGATE;
import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_GROUP_BY;
import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_JOIN;
import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_LOAD;
import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_PROJECT;
import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_SELECT;
import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_SINK;
import static com.testbed.springConfiguration.OperationsNamesConstants.PHYSICAL_UNION;

@Configuration
@Profile(TIMED_SPARK)
public class TimedSparkInvocablesConfiguration {
    private static final String APP_NAME = "Testbed";
    private static final String LOCAL_DIRECTORY_CONFIG = "spark.local.dir";
    private static final String LOCAL_DIRECTORY_PATH = "/tmp/.spark_local_directory";
    private static final String COMPRESSION_CODEC_CONFIG = "spark.sql.parquet.compression.codec";
    private static final String UNCOMPRESSED = "uncompressed";

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
    public Invokable sparkSinkInvokable(FileSystem fileSystem) {
        return new SinkSparkOperation(fileSystem);
    }

    @Bean
    public SparkSession sparkSession(@Value("${clusterMode.spark}") String sparkClusterMode) {
        return SparkSession.builder()
                .appName(APP_NAME)
                .master(sparkClusterMode)
                .config(LOCAL_DIRECTORY_CONFIG, LOCAL_DIRECTORY_PATH)
                .config(COMPRESSION_CODEC_CONFIG, UNCOMPRESSED)
                .getOrCreate();
    }

    @Bean
    public MonitorComposer monitorComposer(ChronometerMonitor chronometerMonitor,
                                           DistributedFileSystemMonitor distributedFileSystemMonitor) {
        // Leftmost monitor is the one that will get executed first. In this case, it is fundamental that the
        // chronometer gets executed first to avoid to interfere in execution time.
        return new MonitorComposer(Arrays.asList(chronometerMonitor, distributedFileSystemMonitor));
    }
}
