package com.testbed.boundary.parameters;

import com.testbed.boundary.parameters.cli.CLIInputInputParametersParser;
import com.testbed.boundary.parameters.cli.options.CLIOption;
import com.testbed.boundary.parameters.cli.options.LocalCLIOption;
import com.testbed.boundary.parameters.cli.options.FrameworkCLIOption;
import com.testbed.boundary.parameters.cli.options.OutputCLIOption;
import com.testbed.boundary.parameters.cli.options.PipelineCLIOption;
import com.testbed.boundary.parameters.cli.options.SheetNameCLIOption;
import com.testbed.boundary.parameters.cli.options.TolerableErrorPercentageCLIOption;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import java.util.Arrays;
import java.util.List;

public class InputParametersParserFactory {
    public static InputParametersParser getParametersParser() {
        return new CLIInputInputParametersParser(getCLIOptions(), new Options(), new DefaultParser(), new HelpFormatter());
    }

    private static List<CLIOption> getCLIOptions() {
        return Arrays.asList(new FrameworkCLIOption(),
                new LocalCLIOption(),
                new OutputCLIOption(),
                new PipelineCLIOption(),
                new SheetNameCLIOption(),
                new TolerableErrorPercentageCLIOption());
    }
}
