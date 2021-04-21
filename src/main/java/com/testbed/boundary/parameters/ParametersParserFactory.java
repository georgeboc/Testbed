package com.testbed.boundary.parameters;

import com.testbed.boundary.parameters.cli.CLIParametersParser;
import com.testbed.boundary.parameters.cli.options.CLIOption;
import com.testbed.boundary.parameters.cli.options.FrameworkCLIOption;
import com.testbed.boundary.parameters.cli.options.OutputCLIOption;
import com.testbed.boundary.parameters.cli.options.PipelineCLIOption;
import com.testbed.boundary.parameters.cli.options.TabNameCLIOption;
import com.testbed.boundary.parameters.cli.options.TolerableErrorPercentageCLIOption;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import java.util.Arrays;
import java.util.List;

public class ParametersParserFactory {
    public static ParametersParser getParametersParser() {
        return new CLIParametersParser(getCLIOptions(), new Options(), new DefaultParser(), new HelpFormatter());
    }

    private static List<CLIOption> getCLIOptions() {
        return Arrays.asList(new FrameworkCLIOption(),
                new OutputCLIOption(),
                new PipelineCLIOption(),
                new TabNameCLIOption(),
                new TolerableErrorPercentageCLIOption());
    }
}
