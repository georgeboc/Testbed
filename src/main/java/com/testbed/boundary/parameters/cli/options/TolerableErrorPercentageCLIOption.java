package com.testbed.boundary.parameters.cli.options;

import com.testbed.entities.parameters.InputParameters;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.lang3.StringUtils;

import static com.testbed.boundary.parameters.cli.options.CLIOptionConstants.HAS_ARG;
import static com.testbed.boundary.parameters.cli.options.CLIOptionConstants.NOT_REQUIRED;

public class TolerableErrorPercentageCLIOption implements CLIOption {
    private static final String DEFAULT_TOLERABLE_ERROR_PERCENTAGE = "5.0";
    private static final String E = "e";
    private static final String TOLERABLE_ERROR_PERCENTAGE = "tolerable-error-percentage";
    private static final String DESCRIPTION = "Tolerable error percentage. Default value is: " +
            DEFAULT_TOLERABLE_ERROR_PERCENTAGE;

    @Override
    public Option getOption() {
        Option tolerableErrorPercentageOption = new Option(E, TOLERABLE_ERROR_PERCENTAGE, HAS_ARG, DESCRIPTION);
        tolerableErrorPercentageOption.setRequired(NOT_REQUIRED);
        return tolerableErrorPercentageOption;
    }

    @Override
    public void addParameter(CommandLine commandLine, InputParameters.InputParametersBuilder inputParametersBuilder) {
        String parsedTolerableErrorPercentage = commandLine.getOptionValue(TOLERABLE_ERROR_PERCENTAGE);
        String tolerableErrorPercentageString = StringUtils.defaultString(parsedTolerableErrorPercentage,
                DEFAULT_TOLERABLE_ERROR_PERCENTAGE);
        inputParametersBuilder.tolerableErrorPercentage(Double.parseDouble(tolerableErrorPercentageString));
    }
}
