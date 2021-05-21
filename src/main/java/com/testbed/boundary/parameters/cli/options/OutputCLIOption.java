package com.testbed.boundary.parameters.cli.options;

import com.testbed.entities.parameters.InputParameters;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;

import static com.testbed.boundary.parameters.cli.options.CLIOptionConstants.HAS_ARG;
import static com.testbed.boundary.parameters.cli.options.CLIOptionConstants.REQUIRED;

public class OutputCLIOption implements CLIOption {
    private static final String O = "o";
    private static final String OUTPUT = "output";
    private static final String DESCRIPTION = "Output file path";

    @Override
    public Option getOption() {
        Option outputOption = new Option(O, OUTPUT, HAS_ARG, DESCRIPTION);
        outputOption.setRequired(REQUIRED);
        return outputOption;
    }

    @Override
    public void addParameter(final CommandLine commandLine,
                             final InputParameters.InputParametersBuilder inputParametersBuilder) {
        inputParametersBuilder.outputPath(commandLine.getOptionValue(OUTPUT));
    }
}
