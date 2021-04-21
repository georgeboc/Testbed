package com.testbed.boundary.parameters;

import com.testbed.entities.parameters.Parameters;

public interface ParametersParser {
    Parameters getParameters(String[] arguments);
}
