package com.testbed.boundary.configurations;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EnvironmentPropertiesFilenames {
    LOCAL("application-local.properties"),
    CLUSTER("application-cluster.properties");

    private final String filename;
}
