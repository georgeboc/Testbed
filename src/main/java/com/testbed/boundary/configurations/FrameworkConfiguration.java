package com.testbed.boundary.configurations;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.testbed.springConfiguration.ApplicationConfiguration.INSTRUMENTED;
import static com.testbed.springConfiguration.ApplicationConfiguration.TIMED;

@RequiredArgsConstructor
@Getter
public enum FrameworkConfiguration {
    TimedMapReduce("MapReduceComponentScan.xml", TIMED),
    TimedSpark("SparkComponentScan.xml", TIMED),
    InstrumentedMapReduce("InstrumentedMapReduceComponentScan.xml", INSTRUMENTED),
    InstrumentedSpark("InstrumentedSparkComponentScan.xml", INSTRUMENTED);

    private final String configurationFile;
    private final String interactorType;
}
