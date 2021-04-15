package com.testbed.boundary.invocations.mapReduce;

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
    public static final String PATH_PREFIX = "/tmp/mapReduceExecution/";
    public static final boolean VERBOSE = true;
    private static final boolean DELETE_RECURSIVELY = true;
    private static final String JOB_NAME = "LocalMapReduce";

    private final Configuration configuration;

    public Job createMapperOnlyJobWithUnaryInputs(final UnaryOperationJobConfiguration jobConfiguration) throws IOException {
        cleanUpOldResults(jobConfiguration);
        Job job = createUnarySourceJob(jobConfiguration);
        job.setMapperClass(jobConfiguration.getMapperClass());
        job.setNumReduceTasks(0);
        return job;
    }

    public Job createMapperReducerJobWithUnaryInputs(final UnaryOperationJobConfiguration jobConfiguration) throws IOException {
        cleanUpOldResults(jobConfiguration);
        Job job = createUnarySourceJob(jobConfiguration);
        job.setMapOutputKeyClass(jobConfiguration.getMapOutputKeyClass());
        job.setMapOutputValueClass(jobConfiguration.getMapOutputValueClass());
        job.setMapperClass(jobConfiguration.getMapperClass());
        job.setReducerClass(jobConfiguration.getReducerClass());
        return job;
    }

    public Job createMapperReducerJobWithBinaryInputs(final BinaryOperationJobConfiguration jobConfiguration) throws IOException {
        cleanUpOldResults(jobConfiguration);
        Job job = createBinarySourceJob(jobConfiguration);
        job.setMapOutputKeyClass(jobConfiguration.getMapOutputKeyClass());
        job.setMapOutputValueClass(jobConfiguration.getMapOutputValueClass());
        job.setReducerClass(jobConfiguration.getReducerClass());
        return job;
    }

    public Job createMapperCombinerReducerJobWithUnaryInputs(final UnaryOperationJobConfiguration jobConfiguration) throws IOException {
        cleanUpOldResults(jobConfiguration);
        Job job = createUnarySourceJob(jobConfiguration);
        job.setMapOutputKeyClass(jobConfiguration.getMapOutputKeyClass());
        job.setMapOutputValueClass(jobConfiguration.getMapOutputValueClass());
        job.setMapperClass(jobConfiguration.getMapperClass());
        job.setCombinerClass(jobConfiguration.getCombinerClass());
        job.setReducerClass(jobConfiguration.getReducerClass());
        return job;
    }

    public Job createMapperCombinerReducerJobWithBinaryInputs(final BinaryOperationJobConfiguration jobConfiguration) throws IOException {
        cleanUpOldResults(jobConfiguration);
        Job job = createBinarySourceJob(jobConfiguration);
        job.setMapOutputKeyClass(jobConfiguration.getMapOutputKeyClass());
        job.setMapOutputValueClass(jobConfiguration.getMapOutputValueClass());
        job.setCombinerClass(jobConfiguration.getCombinerClass());
        job.setReducerClass(jobConfiguration.getReducerClass());
        return job;
    }

    private Job createUnarySourceJob(UnaryOperationJobConfiguration jobConfiguration) throws IOException {
        Job job = Job.getInstance(configuration, JOB_NAME);
        job.setOutputKeyClass(jobConfiguration.getOutputKeyClass());
        job.setOutputValueClass(jobConfiguration.getOutputValueClass());
        job.setInputFormatClass(jobConfiguration.getInputFormatClass());
        job.setOutputFormatClass(jobConfiguration.getOutputFormatClass());
        FileInputFormat.addInputPath(job, new Path(jobConfiguration.getInputPath()));
        FileOutputFormat.setOutputPath(job, new Path(jobConfiguration.getOutputPath()));
        return job;
    }

    private Job createBinarySourceJob(BinaryOperationJobConfiguration jobConfiguration) throws IOException {
        Job job = Job.getInstance(configuration, JOB_NAME);
        job.setOutputKeyClass(jobConfiguration.getOutputKeyClass());
        job.setOutputValueClass(jobConfiguration.getOutputValueClass());
        MultipleInputs.addInputPath(job,
                new Path(jobConfiguration.getLeftInputPath()),
                jobConfiguration.getLeftInputFormatClass(),
                jobConfiguration.getLeftMapperClass());
        MultipleInputs.addInputPath(job,
                new Path(jobConfiguration.getRightInputPath()),
                jobConfiguration.getRightInputFormatClass(),
                jobConfiguration.getRightMapperClass());
        job.setOutputFormatClass(jobConfiguration.getOutputFormatClass());
        FileOutputFormat.setOutputPath(job, new Path(jobConfiguration.getOutputPath()));
        return job;
    }

    private void cleanUpOldResults(final OperationJobConfiguration jobConfiguration) throws IOException {
        FileSystem fileSystem = FileSystem.get(configuration);
        fileSystem.delete(new Path(jobConfiguration.getOutputPath()), DELETE_RECURSIVELY);
    }
}
