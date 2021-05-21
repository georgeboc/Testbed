package com.testbed.boundary.parameters;

import com.testbed.entities.parameters.InputParameters;

public interface InputParametersParser {
    InputParameters getParameters(final String[] arguments);
}
