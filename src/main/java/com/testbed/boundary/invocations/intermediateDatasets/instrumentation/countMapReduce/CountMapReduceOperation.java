package com.testbed.boundary.invocations.intermediateDatasets.instrumentation.countMapReduce;

import com.testbed.boundary.invocations.frameworks.mapReduce.JobConfigurationCommons;
import com.testbed.boundary.invocations.frameworks.mapReduce.UnaryOperationJobConfiguration;
import com.testbed.boundary.utils.DirectoryUtils;
import lombok.RequiredArgsConstructor;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.parquet.hadoop.example.ExampleInputFormat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.regex.Pattern;

import static com.testbed.boundary.invocations.frameworks.mapReduce.JobConfigurationCommons.PATH_PREFIX;
import static com.testbed.boundary.invocations.frameworks.mapReduce.JobConfigurationCommons.VERBOSE;

@RequiredArgsConstructor
public class CountMapReduceOperation {
    private static final String COUNT = "count";
    private static final int FIRST = 0;
    private static final String ZERO_COUNT = "0";
    private static final boolean IS_DELETABLE = true;

    private final DirectoryUtils directoryUtils;
    private final FileSystem fileSystem;
    private final JobConfigurationCommons jobConfigurationCommons;

    public long count(final String inputPath) {
        try {
            return tryRunJob(inputPath);
        } catch (IOException | ClassNotFoundException | InterruptedException exception) {
            throw new RuntimeException(exception);
        }
    }

    private long tryRunJob(final String inputPath) throws IOException, InterruptedException, ClassNotFoundException {
        String outputPath = PATH_PREFIX + COUNT;
        Job job = jobConfigurationCommons.createMapperCombinerReducerJobWithUnaryInputs(UnaryOperationJobConfiguration.builder()
                .inputPath(inputPath)
                .outputPath(outputPath)
                .mapOutputKeyClass(NullWritable.class)
                .mapOutputValueClass(LongWritable.class)
                .outputKeyClass(NullWritable.class)
                .outputValueClass(LongWritable.class)
                .mapperClass(CountJar.CountMapper.class)
                .combinerClass(CountJar.CountReducer.class)
                .reducerClass(CountJar.CountReducer.class)
                .inputFormatClass(ExampleInputFormat.class)
                .outputFormatClass(TextOutputFormat.class)
                .jar(CountJar.class)
                .build());
        job.setNumReduceTasks(1);
        job.waitForCompletion(VERBOSE);
        String resultFilePath = directoryUtils.tryGetFilesInDirectoryByPattern(outputPath,
                Pattern.compile(".*part-r-\\d+$")).get(FIRST);
        return Long.parseLong(Optional.ofNullable(readLine(resultFilePath)).orElse(ZERO_COUNT));
    }

    private String readLine(String filename) throws IOException {
        FSDataInputStream inputStream = fileSystem.open(new Path(filename));
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = bufferedReader.readLine();
        inputStream.close();
        return line;
    }
}
