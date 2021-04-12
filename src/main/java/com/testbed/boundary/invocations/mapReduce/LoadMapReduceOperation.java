package com.testbed.boundary.invocations.mapReduce;

import com.testbed.boundary.invocations.InvocationParameters;
import com.testbed.boundary.invocations.Invokable;
import com.testbed.boundary.invocations.Nameable;
import com.testbed.boundary.invocations.intermediateDatasets.IntermediateDataset;
import com.testbed.boundary.invocations.intermediateDatasets.ReferenceIntermediateDataset;
import com.testbed.entities.operations.physical.PhysicalLoad;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.parquet.example.data.Group;
import org.apache.parquet.hadoop.example.ExampleInputFormat;

import java.io.IOException;

import static com.testbed.boundary.invocations.OperationsConstants.LOAD;
import static com.testbed.boundary.invocations.mapReduce.JobConfigurationCommons.PATH_PREFIX;
import static com.testbed.boundary.invocations.mapReduce.JobConfigurationCommons.VERBOSE;

public class LoadMapReduceOperation implements Invokable, Nameable {
    @Getter
    private final String name = LOAD;

    @Override
    public IntermediateDataset invoke(InvocationParameters invocationParameters) {
        PhysicalLoad physicalLoad = (PhysicalLoad) invocationParameters.getPhysicalOperation();
        return new ReferenceIntermediateDataset(physicalLoad.getDatasetDirectoryPath());
    }
}
