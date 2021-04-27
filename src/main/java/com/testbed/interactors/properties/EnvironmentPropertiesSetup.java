package com.testbed.interactors.properties;

import com.testbed.boundary.configurations.EnvironmentPropertiesFilename;

public class EnvironmentPropertiesSetup {
    private static final String ENVIRONMENT_PROPERTIES_FILENAME = "environment_properties_filename";

    public static void setup(boolean isLocalEnvironment) {
        if (isLocalEnvironment) {
            System.setProperty(ENVIRONMENT_PROPERTIES_FILENAME, EnvironmentPropertiesFilename.LOCAL.getFilename());
        } else {
            System.setProperty(ENVIRONMENT_PROPERTIES_FILENAME,  EnvironmentPropertiesFilename.CLUSTER.getFilename());
        }
    }
}
