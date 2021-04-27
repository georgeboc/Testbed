package com.testbed.boundary.invocations.frameworks.mapReduce;

import lombok.Builder;
import lombok.Data;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.OutputFormat;
import org.apache.hadoop.mapreduce.Reducer;

@Data
@Builder
public class BinaryOperationJobConfiguration implements OperationJobConfiguration {
    private final String leftInputPath;
    private final String rightInputPath;
    private final String outputPath;
    private final Class<? extends Mapper> leftMapperClass;
    private final Class<? extends Mapper> rightMapperClass;
    private final Class<? extends Reducer> combinerClass;
    private final Class<? extends Reducer> reducerClass;
    private final Class<? extends InputFormat> leftInputFormatClass;
    private final Class<? extends InputFormat> rightInputFormatClass;
    private final Class<? extends OutputFormat> outputFormatClass;
    private final Class<?> mapOutputKeyClass;
    private final Class<?> mapOutputValueClass;
    private final Class<?> outputKeyClass;
    private final Class<?> outputValueClass;
}
