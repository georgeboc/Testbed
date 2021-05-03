package com.testbed.boundary.invocations.frameworks.mapReduce;

import lombok.RequiredArgsConstructor;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

@RequiredArgsConstructor
public class JobConfigurationCommons {
    public static final String PATH_PREFIX = "mapReduceExecution/";
    public static final boolean VERBOSE = true;
    private static final boolean DELETE_RECURSIVELY = true;
    private static final String JOB_NAME = "LocalMapReduce";
    private static final int ZERO_REDUCE_TASKS = 0;

    private final Configuration configuration;

    public Job createMapperOnlyJobWithUnaryInputs(final UnaryOperationJobConfiguration jobConfiguration) throws IOException {
        cleanUpOldResults(jobConfiguration.getOutputPath());
        Job job = createUnarySourceJob(jobConfiguration);
        job.setMapperClass(jobConfiguration.getMapperClass());
        job.setNumReduceTasks(ZERO_REDUCE_TASKS);
        return job;
    }

    public Job createMapperReducerJobWithUnaryInputs(final UnaryOperationJobConfiguration jobConfiguration) throws IOException {
        cleanUpOldResults(jobConfiguration.getOutputPath());
        Job job = createUnarySourceJob(jobConfiguration);
        setMapperOutputClasses(jobConfiguration, job);
        job.setMapperClass(jobConfiguration.getMapperClass());
        job.setReducerClass(jobConfiguration.getReducerClass());
        return job;
    }

    public Job createMapperReducerJobWithBinaryInputs(final BinaryOperationJobConfiguration jobConfiguration) throws IOException {
        cleanUpOldResults(jobConfiguration.getOutputPath());
        Job job = createBinarySourceJob(jobConfiguration);
        setMapperOutputClasses(jobConfiguration, job);
        job.setReducerClass(jobConfiguration.getReducerClass());
        return job;
    }

    public Job createMapperCombinerReducerJobWithUnaryInputs(final UnaryOperationJobConfiguration jobConfiguration) throws IOException {
        cleanUpOldResults(jobConfiguration.getOutputPath());
        Job job = createUnarySourceJob(jobConfiguration);
        setMapperOutputClasses(jobConfiguration, job);
        job.setMapperClass(jobConfiguration.getMapperClass());
        job.setCombinerClass(jobConfiguration.getCombinerClass());
        job.setReducerClass(jobConfiguration.getReducerClass());
        return job;
    }

    public Job createMapperCombinerReducerJobWithBinaryInputs(final BinaryOperationJobConfiguration jobConfiguration) throws IOException {
        cleanUpOldResults(jobConfiguration.getOutputPath());
        Job job = createBinarySourceJob(jobConfiguration);
        setMapperOutputClasses(jobConfiguration, job);
        job.setCombinerClass(jobConfiguration.getCombinerClass());
        job.setReducerClass(jobConfiguration.getReducerClass());
        return job;
    }

    private void cleanUpOldResults(final String outputPath) throws IOException {
        FileSystem fileSystem = FileSystem.get(configuration);
        fileSystem.delete(new Path(outputPath), DELETE_RECURSIVELY);
    }

    private void setMapperOutputClasses(OperationJobConfiguration jobConfiguration, Job job) {
        job.setMapOutputKeyClass(jobConfiguration.getMapOutputKeyClass());
        job.setMapOutputValueClass(jobConfiguration.getMapOutputValueClass());
    }

    private Job createUnarySourceJob(UnaryOperationJobConfiguration jobConfiguration) throws IOException {
        Job job = Job.getInstance(configuration, JOB_NAME);
        job.setInputFormatClass(jobConfiguration.getInputFormatClass());
        FileInputFormat.addInputPath(job, new Path(jobConfiguration.getInputPath()));
        setJobOutputClasses(jobConfiguration, job);
        job.setJarByClass(jobConfiguration.getJar());
        return job;
    }

    private Job createBinarySourceJob(BinaryOperationJobConfiguration jobConfiguration) throws IOException {
        Job job = Job.getInstance(configuration, JOB_NAME);
        MultipleInputs.addInputPath(job,
                new Path(jobConfiguration.getLeftInputPath()),
                jobConfiguration.getLeftInputFormatClass(),
                jobConfiguration.getLeftMapperClass());
        MultipleInputs.addInputPath(job,
                new Path(jobConfiguration.getRightInputPath()),
                jobConfiguration.getRightInputFormatClass(),
                jobConfiguration.getRightMapperClass());
        setJobOutputClasses(jobConfiguration, job);
        job.setJarByClass(jobConfiguration.getJar());
        return job;
    }

    private void setJobOutputClasses(OperationJobConfiguration jobConfiguration, Job job) {
        job.setOutputKeyClass(jobConfiguration.getOutputKeyClass());
        job.setOutputValueClass(jobConfiguration.getOutputValueClass());
        job.setOutputFormatClass(jobConfiguration.getOutputFormatClass());
        FileOutputFormat.setOutputPath(job, new Path(jobConfiguration.getOutputPath()));
    }
}
