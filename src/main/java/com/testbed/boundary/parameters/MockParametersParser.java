package com.testbed.boundary.parameters;

import com.testbed.boundary.configurations.SparkConfiguration;
import com.testbed.entities.parameters.Parameters;

//TODO: Drop implementation
public class MockParametersParser implements ParametersParser {

    @Override
    public Parameters getParameters(String[] arguments) {
        return Parameters.builder()
                .configuration(SparkConfiguration.INSTRUMENTED)
                .pipelineFileName("pipelines/pipeline.json")
                .outputPath("output/operation_instrumentations.csv")
                .tolerableErrorPercentage(5.0)
                .build();
    }
}
