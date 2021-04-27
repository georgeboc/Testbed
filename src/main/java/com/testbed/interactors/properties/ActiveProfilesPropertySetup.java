package com.testbed.interactors.properties;

import com.testbed.boundary.invocations.frameworks.FrameworkName;
import com.testbed.interactors.InteractorName;

public class ActiveProfilesPropertySetup {
    private static final String ACTIVE_SPRING_PROFILES = "spring.profiles.active";

    public static void setup(FrameworkName frameworkName, boolean isInstrumented) {
        System.setProperty(ACTIVE_SPRING_PROFILES, InteractorName.getInteractor(isInstrumented).name() + frameworkName.name());
    }
}
