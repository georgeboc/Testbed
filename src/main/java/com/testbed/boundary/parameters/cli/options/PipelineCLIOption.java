package com.testbed.boundary.parameters.cli.options;

import com.testbed.entities.parameters.InputParameters;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;

import static com.testbed.boundary.parameters.cli.options.CLIOptionConstants.HAS_ARG;
import static com.testbed.boundary.parameters.cli.options.CLIOptionConstants.REQUIRED;

public class PipelineCLIOption implements CLIOption {
    private static final String P = "p";
    private static final String PIPELINE = "pipeline";
    private static final String DESCRIPTION = "Pipeline file path";

    @Override
    public Option getOption() {
        Option pipelineOption = new Option(P, PIPELINE, HAS_ARG, DESCRIPTION);
        pipelineOption.setRequired(REQUIRED);
        return pipelineOption;
    }

    @Override
    public void addParameter(final CommandLine commandLine,
                             final InputParameters.InputParametersBuilder inputParametersBuilder) {
        inputParametersBuilder.pipelineFileName(commandLine.getOptionValue(PIPELINE));
    }
}
