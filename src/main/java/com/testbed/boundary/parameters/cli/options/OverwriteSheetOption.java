package com.testbed.boundary.parameters.cli.options;

import com.testbed.entities.parameters.InputParameters;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;

import static com.testbed.boundary.parameters.cli.options.CLIOptionConstants.NOT_REQUIRED;
import static com.testbed.boundary.parameters.cli.options.CLIOptionConstants.NO_ARGS;

public class OverwriteSheetOption implements CLIOption {
    private static final String W = "w";
    private static final String OVERWRITE_SHEET = "overwrite-sheet";
    private static final String DESCRIPTION = "If the flag is present and if the sheet exists within the output excel" +
            " file, then the Testbed clears sheet's contents and puts only one execution's information in the output " +
            "excel file. If the tab does not exist or if the flag is not present, then the Testbed writes execution's" +
            " information in the next unwritten position (with a maximum of 3 possible executions).";

    @Override
    public Option getOption() {
        Option environmentOption = new Option(W, OVERWRITE_SHEET, NO_ARGS, DESCRIPTION);
        environmentOption.setRequired(NOT_REQUIRED);
        return environmentOption;
    }

    @Override
    public void addParameter(final CommandLine commandLine,
                             final InputParameters.InputParametersBuilder inputParametersBuilder) {
        inputParametersBuilder.isOverwriteSheetEnabled(commandLine.hasOption(OVERWRITE_SHEET));
    }
}
