package com.testbed.boundary.configurations;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EnvironmentPropertiesFilename {
    LOCAL("application-local.properties"),
    CLUSTER("application-cluster.properties");

    private final String filename;
}
