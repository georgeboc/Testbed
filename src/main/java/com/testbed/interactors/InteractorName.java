package com.testbed.interactors;

import lombok.ToString;

@ToString
public enum InteractorName {
    Instrumented,
    Timed;

    public static final String INSTRUMENTED = "Instrumented";
    public static final String TIMED = "Timed";

    public static InteractorName getInteractor(final boolean isInstrumented) {
        if (isInstrumented) {
            return Instrumented;
        }
        return Timed;
    }
}
