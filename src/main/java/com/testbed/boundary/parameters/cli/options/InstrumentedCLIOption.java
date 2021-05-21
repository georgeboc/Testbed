package com.testbed.boundary.parameters.cli.options;

import com.testbed.entities.parameters.InputParameters;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;

import static com.testbed.boundary.parameters.cli.options.CLIOptionConstants.NOT_REQUIRED;
import static com.testbed.boundary.parameters.cli.options.CLIOptionConstants.NO_ARGS;

public class InstrumentedCLIOption implements CLIOption {
    private static final String I = "i";
    private static final String INSTRUMENTED = "instrumented";
    private static final String DESCRIPTION = "If this flag is present, the Testbed will use the instrumented " +
            "invocations. Without this flag, the Testbed will use the invocations required to measure time";

    @Override
    public Option getOption() {
        Option frameworkOption = new Option(I, INSTRUMENTED, NO_ARGS, DESCRIPTION);
        frameworkOption.setRequired(NOT_REQUIRED);
        return frameworkOption;
    }

    @Override
    public void addParameter(final CommandLine commandLine,
                             final InputParameters.InputParametersBuilder inputParametersBuilder) {
        inputParametersBuilder.isInstrumented(commandLine.hasOption(INSTRUMENTED));
    }
}
