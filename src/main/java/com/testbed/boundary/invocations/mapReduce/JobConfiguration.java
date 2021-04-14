package com.testbed.boundary.invocations.mapReduce;

import lombok.Builder;
import lombok.Data;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.OutputFormat;
import org.apache.hadoop.mapreduce.Reducer;

@Data
@Builder
public class JobConfiguration {
    private final String inputPath;
    private final String outputPath;
    private final Class<? extends Mapper> mapperClass;
    private final Class<? extends Reducer> combinerClass;
    private final Class<? extends Reducer> reducerClass;
    private final Class<? extends InputFormat> inputFormatClass;
    private final Class<? extends OutputFormat> outputFormatClass;
    private final Class<?> outputKeyClass;
    private final Class<?> outputValueClass;
}
