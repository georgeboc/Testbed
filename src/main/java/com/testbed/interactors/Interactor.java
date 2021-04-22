package com.testbed.interactors;

import com.testbed.entities.parameters.InputParameters;

public interface Interactor {
    void execute(InputParameters inputParameters) throws Exception;
}
