package com.testbed.boundary.invocations.mapReduce;

import com.testbed.boundary.utils.ParquetSchemaReader;
import com.testbed.boundary.invocations.InvocationParameters;
import com.testbed.boundary.invocations.Invokable;
import com.testbed.boundary.invocations.Nameable;
import com.testbed.boundary.invocations.intermediateDatasets.IntermediateDataset;
import com.testbed.boundary.invocations.intermediateDatasets.ReferenceIntermediateDataset;
import com.testbed.entities.operations.physical.PhysicalSelect;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.parquet.example.data.Group;
import org.apache.parquet.hadoop.example.ExampleInputFormat;
import org.apache.parquet.hadoop.example.ExampleOutputFormat;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;
import org.apache.parquet.schema.MessageType;

import java.io.IOException;

import static com.testbed.boundary.invocations.OperationsConstants.SELECT;
import static com.testbed.boundary.invocations.mapReduce.JobConfigurationCommons.PATH_PREFIX;
import static com.testbed.boundary.invocations.mapReduce.JobConfigurationCommons.VERBOSE;

@RequiredArgsConstructor
public class SelectMapReduceOperation implements Invokable, Nameable {
    private static final int FIRST = 0;
    private static final String LESS_THAN_OR_EQUAL_VALUE = "lessThanOrEqualValue";
    private static final String COLUMN_INDEX = "columnIndex";
    private final JobConfigurationCommons jobConfigurationCommons;
    private final ParquetSchemaReader parquetSchemaReader;
    @Getter
    private final String name = SELECT;

    @Override
    public IntermediateDataset invoke(InvocationParameters invocationParameters) {
        try {
            return tryRunJob(invocationParameters);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private ReferenceIntermediateDataset tryRunJob(InvocationParameters invocationParameters) throws IOException,
            InterruptedException, ClassNotFoundException {
        PhysicalSelect physicalSelect = (PhysicalSelect) invocationParameters.getPhysicalOperation();
        String inputPath = invocationParameters.getInputIntermediateDatasets().get(FIRST)
                .getValue()
                .get()
                .toString();
        String outputPath = PATH_PREFIX + physicalSelect.getId();
        Job job = jobConfigurationCommons.createMapperOnlyJob(JobConfiguration.builder()
                .inputPath(inputPath)
                .outputPath(outputPath)
                .inputFormatClass(ExampleInputFormat.class)
                .outputFormatClass(ExampleOutputFormat.class)
                .outputKeyClass(LongWritable.class)
                .outputValueClass(Group.class)
                .mapperClass(SelectMapper.class)
                .build());
        MessageType schema = parquetSchemaReader.readSchema(inputPath);
        ExampleOutputFormat.setSchema(job, schema);
        ExampleOutputFormat.setCompression(job, CompressionCodecName.UNCOMPRESSED);
        job.getConfiguration().set(LESS_THAN_OR_EQUAL_VALUE, physicalSelect.getLessThanOrEqualValue());
        job.getConfiguration().setInt(COLUMN_INDEX, schema.getFieldIndex(physicalSelect.getColumnName()));
        job.waitForCompletion(VERBOSE);
        return new ReferenceIntermediateDataset(outputPath);
    }

    private static class SelectMapper extends Mapper<LongWritable, Group, LongWritable, Group> {
        private static final int DEFAULT_VALUE = 0;
        private static final int SKIP_SEPARATOR = 2;

        @Override
        public void map(LongWritable key, Group value, Context context) throws IOException, InterruptedException {
            int columnIndex = context.getConfiguration().getInt(COLUMN_INDEX, DEFAULT_VALUE);
            String line = value.toString();
            String[] fields = line.split("\n");
            int fieldSeparatorPosition = fields[columnIndex].indexOf(':');
            String fieldValue = fields[columnIndex].substring(fieldSeparatorPosition + SKIP_SEPARATOR);
            String lessThanOrEqualValue = context.getConfiguration().get(LESS_THAN_OR_EQUAL_VALUE);
            if (fieldValue.compareTo(lessThanOrEqualValue) <= 0) {
                context.write(key, value);
            }
        }
    }
}
