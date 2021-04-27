package com.testbed.boundary.parameters.cli.options;

import com.testbed.entities.parameters.InputParameters;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;

import static com.testbed.boundary.parameters.cli.options.CLIOptionConstants.NOT_REQUIRED;
import static com.testbed.boundary.parameters.cli.options.CLIOptionConstants.NO_ARGS;

public class LocalCLIOption implements CLIOption {
    private static final String L = "l";
    private static final String LOCAL = "local";
    private static final String DESCRIPTION = "If the flag is present, the Testbed uses the local environment for " +
            "the frameworks. Without this flag, the Testbed uses the cluster environment.";

    @Override
    public Option getOption() {
        Option environmentOption = new Option(L, LOCAL, NO_ARGS, DESCRIPTION);
        environmentOption.setRequired(NOT_REQUIRED);
        return environmentOption;
    }

    @Override
    public void addParameter(CommandLine commandLine, InputParameters.InputParametersBuilder inputParametersBuilder) {
        inputParametersBuilder.isLocalEnvironment(commandLine.hasOption(LOCAL));
    }
}
