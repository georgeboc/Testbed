package com.testbed.boundary.parameters.cli.options;

import com.testbed.boundary.configurations.FrameworkConfiguration;
import com.testbed.entities.parameters.InputParameters;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;

import java.util.Arrays;

import static com.testbed.boundary.parameters.cli.options.CLIOptionConstants.HAS_ARG;
import static com.testbed.boundary.parameters.cli.options.CLIOptionConstants.REQUIRED;

public class FrameworkCLIOption implements CLIOption {
    private static final String F = "f";
    private static final String FRAMEWORK_CONFIGURATION = "framework-configuration";
    private static final String DESCRIPTION = "Data Processing Framework configuration. Available options are: " +
            Arrays.toString(FrameworkConfiguration.values());

    @Override
    public Option getOption() {
        Option frameworkOption = new Option(F, FRAMEWORK_CONFIGURATION, HAS_ARG, DESCRIPTION);
        frameworkOption.setRequired(REQUIRED);
        return frameworkOption;
    }

    @Override
    public void addParameter(CommandLine commandLine, InputParameters.InputParametersBuilder inputParametersBuilder) {
        String parsedFrameworkConfiguration = commandLine.getOptionValue(FRAMEWORK_CONFIGURATION);
        inputParametersBuilder.frameworkConfiguration(FrameworkConfiguration.valueOf(parsedFrameworkConfiguration));
    }
}
