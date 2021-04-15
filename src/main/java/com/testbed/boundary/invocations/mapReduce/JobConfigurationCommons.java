package com.testbed.boundary.invocations.mapReduce;

import lombok.RequiredArgsConstructor;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

@RequiredArgsConstructor
public class JobConfigurationCommons {
    public static final String PATH_PREFIX = "/tmp/mapReduceExecution/";
    public static final boolean VERBOSE = true;
    private static final boolean DELETE_RECURSIVELY = true;
    private static final String JOB_NAME = "LocalMapReduce";

    private final Configuration configuration;

    public Job createMapperOnlyJob(final JobConfiguration jobConfiguration) throws IOException, InterruptedException, ClassNotFoundException {
        cleanUpOldResults(jobConfiguration);
        Job job = createJob(jobConfiguration);
        job.setMapperClass(jobConfiguration.getMapperClass());
        job.setNumReduceTasks(0);
        return job;
    }

    public Job createMapperReducerJob(final JobConfiguration jobConfiguration) throws IOException, InterruptedException, ClassNotFoundException {
        cleanUpOldResults(jobConfiguration);
        Job job = createJob(jobConfiguration);
        job.setMapOutputKeyClass(jobConfiguration.getMapOutputKeyClass());
        job.setMapOutputValueClass(jobConfiguration.getMapOutputValueClass());
        job.setMapperClass(jobConfiguration.getMapperClass());
        job.setReducerClass(jobConfiguration.getReducerClass());
        return job;
    }

    public Job createMapperCombinerReducerJob(final JobConfiguration jobConfiguration) throws IOException, InterruptedException, ClassNotFoundException {
        cleanUpOldResults(jobConfiguration);
        Job job = createJob(jobConfiguration);
        job.setMapOutputKeyClass(jobConfiguration.getMapOutputKeyClass());
        job.setMapOutputValueClass(jobConfiguration.getMapOutputValueClass());
        job.setMapperClass(jobConfiguration.getMapperClass());
        job.setCombinerClass(jobConfiguration.getCombinerClass());
        job.setReducerClass(jobConfiguration.getReducerClass());
        return job;
    }

    private Job createJob(JobConfiguration jobConfiguration) throws IOException {
        Job job = Job.getInstance(configuration, JOB_NAME);
        job.setOutputKeyClass(jobConfiguration.getOutputKeyClass());
        job.setOutputValueClass(jobConfiguration.getOutputValueClass());
        job.setInputFormatClass(jobConfiguration.getInputFormatClass());
        job.setOutputFormatClass(jobConfiguration.getOutputFormatClass());
        FileInputFormat.addInputPath(job, new Path(jobConfiguration.getInputPath()));
        FileOutputFormat.setOutputPath(job, new Path(jobConfiguration.getOutputPath()));
        return job;
    }

    private void cleanUpOldResults(final JobConfiguration jobConfiguration) throws IOException {
        FileSystem fileSystem = FileSystem.get(configuration);
        fileSystem.delete(new Path(jobConfiguration.getOutputPath()), DELETE_RECURSIVELY);
    }
}
