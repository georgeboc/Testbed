package com.testbed.boundary.invocations.intermediateDatasets.instrumentation;

import com.google.common.collect.Streams;
import com.testbed.boundary.utils.DirectoryUtils;
import com.testbed.boundary.invocations.mapReduce.UnaryOperationJobConfiguration;
import com.testbed.boundary.invocations.mapReduce.JobConfigurationCommons;
import lombok.RequiredArgsConstructor;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.SeekableInput;
import org.apache.avro.mapred.FsInput;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.parquet.example.data.Group;
import org.apache.parquet.hadoop.example.ExampleInputFormat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.regex.Pattern;

import static com.testbed.boundary.invocations.mapReduce.JobConfigurationCommons.PATH_PREFIX;
import static com.testbed.boundary.invocations.mapReduce.JobConfigurationCommons.VERBOSE;

@RequiredArgsConstructor
public class CountMapReduce {
    private static final String COUNT = "count";
    private static final int FIRST = 0;
    private static final String ZERO_COUNT = "0";

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

    private long tryRunJob(final String inputPath) throws IOException,
            InterruptedException, ClassNotFoundException {
        String outputPath = PATH_PREFIX + COUNT;
        Job job = jobConfigurationCommons.createMapperCombinerReducerJobWithUnaryInputs(UnaryOperationJobConfiguration.builder()
                .inputPath(inputPath)
                .outputPath(outputPath)
                .mapOutputKeyClass(NullWritable.class)
                .mapOutputValueClass(LongWritable.class)
                .outputKeyClass(NullWritable.class)
                .outputValueClass(LongWritable.class)
                .mapperClass(CountMapper.class)
                .combinerClass(CountReducer.class)
                .reducerClass(CountReducer.class)
                .inputFormatClass(ExampleInputFormat.class)
                .outputFormatClass(TextOutputFormat.class)
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
        return bufferedReader.readLine();
    }

    private static class CountMapper extends Mapper<LongWritable, Group, NullWritable, LongWritable> {
        @Override
        public void map(LongWritable key, Group value, Context context) throws IOException, InterruptedException {
            context.write(NullWritable.get(), new LongWritable(1L));
        }
    }

    private static class CountReducer extends Reducer<NullWritable, LongWritable, NullWritable, LongWritable> {
        @Override
        public void reduce(NullWritable nullWritable, Iterable<LongWritable> iterable, Context context) throws IOException, InterruptedException {
            long sum = Streams.stream(iterable).map(LongWritable::get).reduce(Long::sum).orElse(0L);
            context.write(nullWritable, new LongWritable(sum));
        }
    }
}
