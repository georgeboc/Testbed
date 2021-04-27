package com.testbed.interactors.properties;

import com.testbed.boundary.configurations.EnvironmentPropertiesFilenames;

public class EnvironmentPropertiesSetup {
    private static final String ENVIRONMENT_PROPERTIES_FILENAME = "environment_properties_filename";

    public static void setup(boolean isLocalEnvironment) {
        if (isLocalEnvironment) {
            System.setProperty(ENVIRONMENT_PROPERTIES_FILENAME, EnvironmentPropertiesFilenames.LOCAL.getFilename());
        } else {
            System.setProperty(ENVIRONMENT_PROPERTIES_FILENAME,  EnvironmentPropertiesFilenames.CLUSTER.getFilename());
        }
    }
}
