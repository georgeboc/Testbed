package com.testbed.entities.parameters;

import com.testbed.boundary.configurations.FrameworkConfiguration;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Parameters {
    private final String pipelineFileName;
    private final String outputPath;
    private final String tabName;
    private final double tolerableErrorPercentage;
    private final FrameworkConfiguration frameworkConfiguration;
}
