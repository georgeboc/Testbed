package com.testbed.boundary.parameters.cli.options;

import com.testbed.entities.parameters.InputParameters;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;

public interface CLIOption {
    Option getOption();
    void addParameter(final CommandLine commandLine, final InputParameters.InputParametersBuilder inputParametersBuilder);
}
