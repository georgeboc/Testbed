package com.testbed.boundary.invocations.intermediateDatasets.instrumentation;

import com.testbed.boundary.invocations.intermediateDatasets.IntermediateDataset;
import com.testbed.boundary.invocations.intermediateDatasets.instrumentation.countMapReduce.CountMapReduceOperation;
import com.testbed.boundary.utils.ParquetSchemaReader;
import lombok.RequiredArgsConstructor;
import org.apache.parquet.schema.MessageType;
import org.apache.parquet.schema.Type;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class MapReduceIntermediateDatasetInstrumentation implements IntermediateDatasetInstrumentation {
    private final CountMapReduceOperation countMapReduceOperation;
    private final ParquetSchemaReader parquetSchemaReader;

    @Override
    public long count(final IntermediateDataset intermediateDataset) {
        if (intermediateDataset.getValue().isEmpty()) {
            return 0L;
        }
        return countMapReduceOperation.count(intermediateDataset.getValue().get().toString());
    }

    @Override
    public List<String> getColumnNames(final IntermediateDataset intermediateDataset) {
        if (intermediateDataset.getValue().isEmpty()) {
            return Collections.emptyList();
        }
        MessageType schema = parquetSchemaReader.readSchema(intermediateDataset.getValue().get().toString());
        return schema.getFields().stream()
                .map(Type::getName)
                .collect(Collectors.toList());
    }
}
