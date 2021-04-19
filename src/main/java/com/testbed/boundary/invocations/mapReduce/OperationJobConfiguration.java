package com.testbed.boundary.invocations.mapReduce;

import org.apache.hadoop.mapreduce.OutputFormat;

public interface OperationJobConfiguration {
    String getOutputPath();

    Class<?> getMapOutputKeyClass();
    Class<?> getMapOutputValueClass();

    Class<?> getOutputKeyClass();
    Class<?> getOutputValueClass();
    Class<? extends OutputFormat> getOutputFormatClass();
}
