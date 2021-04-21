package com.testbed.boundary.parameters.cli.options;

import com.testbed.entities.parameters.Parameters;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;

import static com.testbed.boundary.parameters.cli.options.CLIOptionConstants.HAS_ARG;
import static com.testbed.boundary.parameters.cli.options.CLIOptionConstants.REQUIRED;

public class TabNameCLIOption implements CLIOption {
    private static final String T = "t";
    private static final String TAB_NAME = "tab-name";
    private static final String DESCRIPTION = "Tab name in output file path";

    @Override
    public Option getOption() {
        Option tabNameOption = new Option(T, TAB_NAME, HAS_ARG, DESCRIPTION);
        tabNameOption.setRequired(REQUIRED);
        return tabNameOption;
    }

    @Override
    public void addParameter(CommandLine commandLine, Parameters.ParametersBuilder parametersBuilder) {
        parametersBuilder.tabName(commandLine.getOptionValue(TAB_NAME));
    }
}
