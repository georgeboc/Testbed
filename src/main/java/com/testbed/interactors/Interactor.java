package com.testbed.interactors;

import com.testbed.entities.parameters.InputParameters;

public interface Interactor {
    void execute(final InputParameters inputParameters) throws Exception;
}
