package com.testbed.boundary.configurations;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MapReduceConfiguration implements Configuration {
    INSTRUMENTED("InstrumentedMapReduceComponentScan.xml"),
    TIMED("MapReduceComponentScan.xml");

    private final String configurationFile;
}
