package com.testbed.boundary.parameters.cli.options;

import com.testbed.entities.parameters.InputParameters;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;

import static com.testbed.boundary.parameters.cli.options.CLIOptionConstants.HAS_ARG;
import static com.testbed.boundary.parameters.cli.options.CLIOptionConstants.REQUIRED;

public class SheetNameCLIOption implements CLIOption {
    private static final String S = "s";
    private static final String SHEET_NAME = "sheet-name";
    private static final String DESCRIPTION = "Sheet name in output file path";

    @Override
    public Option getOption() {
        Option tabNameOption = new Option(S, SHEET_NAME, HAS_ARG, DESCRIPTION);
        tabNameOption.setRequired(REQUIRED);
        return tabNameOption;
    }

    @Override
    public void addParameter(CommandLine commandLine, InputParameters.InputParametersBuilder inputParametersBuilder) {
        inputParametersBuilder.sheetName(commandLine.getOptionValue(SHEET_NAME));
    }
}
