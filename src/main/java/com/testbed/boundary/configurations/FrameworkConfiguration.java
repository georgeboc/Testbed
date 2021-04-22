package com.testbed.boundary.configurations;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.testbed.boundary.configurations.FrameworkConstants.MAPREDUCE;
import static com.testbed.boundary.configurations.FrameworkConstants.SPARK;
import static com.testbed.springConfiguration.ApplicationConfiguration.INSTRUMENTED;
import static com.testbed.springConfiguration.ApplicationConfiguration.TIMED;

@RequiredArgsConstructor
@Getter
public enum FrameworkConfiguration {
    TimedMapReduce("MapReduceComponentScan.xml", TIMED, MAPREDUCE),
    TimedSpark("SparkComponentScan.xml", TIMED, SPARK),
    InstrumentedMapReduce("InstrumentedMapReduceComponentScan.xml", INSTRUMENTED, MAPREDUCE),
    InstrumentedSpark("InstrumentedSparkComponentScan.xml", INSTRUMENTED, SPARK);

    private final String configurationFile;
    private final String interactorType;
    private final String frameworkName;
}
