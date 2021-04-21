package com.testbed.boundary.parameters;

import com.testbed.entities.parameters.Parameters;

import java.lang.reflect.InvocationTargetException;

public interface ParametersParser {
    Parameters getParameters(String[] arguments);
}
