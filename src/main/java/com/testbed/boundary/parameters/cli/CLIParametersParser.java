package com.testbed.boundary.parameters.cli;

import com.testbed.boundary.parameters.ParametersParser;
import com.testbed.boundary.parameters.cli.options.CLIOption;
import com.testbed.entities.parameters.Parameters;
import lombok.RequiredArgsConstructor;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.util.List;

@RequiredArgsConstructor
public class CLIParametersParser implements ParametersParser {
    private static final String COMMAND_LINE_SYNTAX = "java -jar Testbed-1.0-SNAPSHOT-jar-with-dependencies.jar [options]";
    private final List<CLIOption> cliOptions;
    private final Options options;
    private final CommandLineParser commandLineParser;
    private final HelpFormatter helpFormatter;

    @Override
    public Parameters getParameters(String[] arguments) {
        cliOptions.forEach(cliOption -> options.addOption(cliOption.getOption()));
        CommandLine commandLine = tryParseArguments(arguments);
        Parameters.ParametersBuilder parametersBuilder = Parameters.builder();
        cliOptions.forEach(cliOption -> cliOption.addParameter(commandLine, parametersBuilder));
        return parametersBuilder.build();
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
