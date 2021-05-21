package com.testbed.boundary.parameters.cli.options;

import com.testbed.boundary.invocations.frameworks.FrameworkName;
import com.testbed.entities.parameters.InputParameters;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;

import static com.testbed.boundary.parameters.cli.options.CLIOptionConstants.HAS_ARG;
import static com.testbed.boundary.parameters.cli.options.CLIOptionConstants.REQUIRED;

public class FrameworkCLIOption implements CLIOption {
    private static final String F = "f";
    private static final String FRAMEWORK_NAME = "framework-name";
    private static final String DESCRIPTION = "Data Processing Framework's name. Available options are: " + FrameworkName.getAllValues();

    @Override
    public Option getOption() {
        Option frameworkOption = new Option(F, FRAMEWORK_NAME, HAS_ARG, DESCRIPTION);
        frameworkOption.setRequired(REQUIRED);
        return frameworkOption;
    }

    @Override
    public void addParameter(final CommandLine commandLine,
                             final InputParameters.InputParametersBuilder inputParametersBuilder) {
        String parsedFrameworkName = commandLine.getOptionValue(FRAMEWORK_NAME);
        inputParametersBuilder.frameworkName(FrameworkName.valueOf(parsedFrameworkName));
    }
}
