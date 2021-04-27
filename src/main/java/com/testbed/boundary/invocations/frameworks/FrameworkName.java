package com.testbed.boundary.invocations.frameworks;

import lombok.ToString;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ToString
public enum FrameworkName {
    MapReduce,
    Spark;

    public static List<String> getAllValues() {
        return Arrays.stream(FrameworkName.values()).map(Enum::name).collect(Collectors.toList());
    }
}
