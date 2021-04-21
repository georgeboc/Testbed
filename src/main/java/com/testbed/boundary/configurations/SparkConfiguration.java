package com.testbed.boundary.configurations;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SparkConfiguration implements Configuration {
    INSTRUMENTED("InstrumentedSparkComponentScan.xml"),
    TIMED("SparkComponentScan.xml");

    private final String configurationFile;
}
