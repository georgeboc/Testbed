package com.testbed.boundary.invocations.mapReduce;

import com.google.common.collect.Streams;
import com.testbed.boundary.invocations.InvocationParameters;
import com.testbed.boundary.invocations.Operation;
import com.testbed.boundary.invocations.intermediateDatasets.IntermediateDataset;
import com.testbed.boundary.invocations.intermediateDatasets.ReferenceIntermediateDataset;
import com.testbed.boundary.utils.ParquetSchemaReader;
import com.testbed.entities.operations.physical.PhysicalAggregate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.parquet.example.data.Group;
import org.apache.parquet.example.data.simple.SimpleGroup;
import org.apache.parquet.hadoop.example.ExampleInputFormat;
import org.apache.parquet.hadoop.example.ExampleOutputFormat;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;
import org.apache.parquet.schema.GroupType;
import org.apache.parquet.schema.MessageType;
import org.apache.parquet.schema.PrimitiveType;
import org.apache.parquet.schema.Type;

import java.io.IOException;

import static com.testbed.boundary.invocations.OperationsConstants.AGGREGATE;
import static com.testbed.boundary.invocations.mapReduce.JobConfigurationCommons.PATH_PREFIX;
import static com.testbed.boundary.invocations.mapReduce.JobConfigurationCommons.VERBOSE;

@RequiredArgsConstructor
public class SumAggregateMapReduceOperation implements Operation {
    private static final int FIRST = 0;
    private static final String AGGREGATE_COLUMN_INDEX = "aggregateColumnIndex";
    private final JobConfigurationCommons jobConfigurationCommons;
    private final ParquetSchemaReader parquetSchemaReader;
    @Getter
    private final String name = AGGREGATE;

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
        PhysicalAggregate physicalAggregate = (PhysicalAggregate) invocationParameters.getPhysicalOperation();
        String inputPath = invocationParameters.getInputIntermediateDatasets().get(FIRST)
                .getValue()
                .get()
                .toString();
        String outputPath = PATH_PREFIX + physicalAggregate.getId();
        Job job = jobConfigurationCommons.createMapperCombinerReducerJobWithUnaryInputs(UnaryOperationJobConfiguration.builder()
                .inputPath(inputPath)
                .outputPath(outputPath)
                .inputFormatClass(ExampleInputFormat.class)
                .outputFormatClass(ExampleOutputFormat.class)
                .mapOutputKeyClass(NullWritable.class)
                .mapOutputValueClass(DoubleWritable.class)
                .outputKeyClass(LongWritable.class)
                .outputValueClass(Group.class)
                .mapperClass(SumAggregateMapper.class)
                .combinerClass(SumAggregateCombiner.class)
                .reducerClass(SumAggregateReducer.class)
                .build());
        MessageType inputSchema = parquetSchemaReader.readSchema(inputPath);
        MessageType aggregateSchema = new MessageType(inputSchema.getName(),
                inputSchema.getType(physicalAggregate.getAggregationColumnName()));
        ExampleOutputFormat.setSchema(job, aggregateSchema);
        ExampleOutputFormat.setCompression(job, CompressionCodecName.UNCOMPRESSED);
        int aggregateColumnIndex = inputSchema.getFieldIndex(physicalAggregate.getAggregationColumnName());
        job.getConfiguration().setInt(AGGREGATE_COLUMN_INDEX, aggregateColumnIndex);
        job.waitForCompletion(VERBOSE);
        return new ReferenceIntermediateDataset(outputPath);
    }

    private static class SumAggregateMapper extends Mapper<LongWritable, Group, NullWritable, DoubleWritable> {
        private static final int DEFAULT_POSITION = 0;
        private static final int DEFAULT_VALUE = 0;

        @Override
        public void map(LongWritable key, Group value, Context context) throws IOException, InterruptedException {
            int aggregateColumnIndex = context.getConfiguration().getInt(AGGREGATE_COLUMN_INDEX, DEFAULT_VALUE);
            double columnValue = Double.parseDouble(value.getString(aggregateColumnIndex, DEFAULT_POSITION));
            context.write(NullWritable.get(), new DoubleWritable(columnValue));
        }
    }

    private static class SumAggregateCombiner extends Reducer<NullWritable, DoubleWritable, NullWritable, DoubleWritable> {
        private static final double INITIAL_VALUE = 0.0;

        @Override
        public void reduce(NullWritable key, Iterable<DoubleWritable> values, Context context) throws IOException, InterruptedException {
            double aggregateValue = Streams.stream(values).map(DoubleWritable::get).reduce(INITIAL_VALUE, Double::sum);
            context.write(key, new DoubleWritable(aggregateValue));
        }
    }

    private static class SumAggregateReducer extends Reducer<NullWritable, DoubleWritable, LongWritable, Group> {
        private static final String AGGREGATE_COLUMN_NAME = "aggregateColumnName";
        private static final String SCHEMA_NAME = "schemaName";
        private static final double INITIAL_VALUE = 0.0;

        @Override
        public void reduce(NullWritable notUsed, Iterable<DoubleWritable> values, Context context) throws IOException, InterruptedException {
            Type columnType = new PrimitiveType(Type.Repetition.OPTIONAL, PrimitiveType.PrimitiveTypeName.BINARY, AGGREGATE_COLUMN_NAME);
            GroupType aggregateGroupType = new GroupType(Type.Repetition.OPTIONAL, SCHEMA_NAME, columnType);
            Group aggregateGroup = new SimpleGroup(aggregateGroupType);
            double aggregateValue = Streams.stream(values).map(DoubleWritable::get).reduce(INITIAL_VALUE, Double::sum);
            aggregateGroup.append(AGGREGATE_COLUMN_NAME, String.valueOf(aggregateValue));
            context.write(null, aggregateGroup);
        }
    }
}
