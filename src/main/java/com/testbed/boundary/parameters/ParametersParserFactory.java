package com.testbed.boundary.parameters;

//TODO: Implement
public class ParametersParserFactory {
    public static ParametersParser getParametersParser() {
        return new MockParametersParser();
    }
}
