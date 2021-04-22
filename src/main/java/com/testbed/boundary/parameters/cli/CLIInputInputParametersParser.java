package com.testbed.boundary.parameters.cli;

import com.testbed.boundary.parameters.InputParametersParser;
import com.testbed.boundary.parameters.cli.options.CLIOption;
import com.testbed.entities.parameters.InputParameters;
import lombok.RequiredArgsConstructor;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.util.List;

@RequiredArgsConstructor
public class CLIInputInputParametersParser implements InputParametersParser {
    private static final String COMMAND_LINE_SYNTAX = "java -jar Testbed-1.0-SNAPSHOT-jar-with-dependencies.jar [options]";
    private final List<CLIOption> cliOptions;
    private final Options options;
    private final CommandLineParser commandLineParser;
    private final HelpFormatter helpFormatter;

    @Override
    public InputParameters getParameters(String[] arguments) {
        cliOptions.forEach(cliOption -> options.addOption(cliOption.getOption()));
        CommandLine commandLine = tryParseArguments(arguments);
        InputParameters.InputParametersBuilder inputParametersBuilder = InputParameters.builder();
        cliOptions.forEach(cliOption -> cliOption.addParameter(commandLine, inputParametersBuilder));
        return inputParametersBuilder.build();
    }

    private CommandLine tryParseArguments(String[] arguments) {
        try {
            return commandLineParser.parse(options, arguments);
        } catch (ParseException exception) {
            helpFormatter.printHelp(COMMAND_LINE_SYNTAX, options);
            throw new RuntimeException(exception);
        }
    }
}
