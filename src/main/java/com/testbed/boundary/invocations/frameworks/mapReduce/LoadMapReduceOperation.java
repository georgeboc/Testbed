package com.testbed.boundary.invocations.frameworks.mapReduce;

import com.testbed.boundary.invocations.InvocationParameters;
import com.testbed.boundary.invocations.Operation;
import com.testbed.boundary.invocations.intermediateDatasets.IntermediateDataset;
import com.testbed.boundary.invocations.intermediateDatasets.ReferenceIntermediateDataset;
import com.testbed.entities.operations.physical.PhysicalLoad;
import lombok.Getter;

import static com.testbed.boundary.invocations.OperationsConstants.LOAD;

public class LoadMapReduceOperation implements Operation {
    private static final String PARSED_DATASET_FILENAME = "parsed_dataset.parquet";
    @Getter
    private final String name = LOAD;

    @Override
    public IntermediateDataset invoke(InvocationParameters invocationParameters) {
        PhysicalLoad physicalLoad = (PhysicalLoad) invocationParameters.getPhysicalOperation();
        return new ReferenceIntermediateDataset(String.format("%s/%s",
                physicalLoad.getDatasetDirectoryPath(),
                PARSED_DATASET_FILENAME));
    }
}
