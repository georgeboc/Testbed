package com.testbed.boundary.invocations.mapReduce;

import com.testbed.boundary.invocations.InvocationParameters;
import com.testbed.boundary.invocations.Operation;
import com.testbed.boundary.invocations.intermediateDatasets.IntermediateDataset;
import com.testbed.boundary.invocations.intermediateDatasets.ReferenceIntermediateDataset;
import com.testbed.boundary.utils.ParquetSchemaReader;
import com.testbed.entities.operations.physical.PhysicalUnion;
import com.testbed.boundary.invocations.mapReduce.MapReduceCommons.Row;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.parquet.example.data.Group;
import org.apache.parquet.hadoop.example.ExampleInputFormat;
import org.apache.parquet.hadoop.example.ExampleOutputFormat;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;
import org.apache.parquet.schema.MessageType;
import org.apache.parquet.schema.MessageTypeParser;

import java.io.IOException;

import static com.testbed.boundary.invocations.OperationsConstants.UNION;
import static com.testbed.boundary.invocations.mapReduce.JobConfigurationCommons.PATH_PREFIX;
import static com.testbed.boundary.invocations.mapReduce.JobConfigurationCommons.VERBOSE;
import static com.testbed.boundary.invocations.mapReduce.MapReduceCommons.RowsParser.parseRow;
import static com.testbed.boundary.invocations.mapReduce.MapReduceCommons.tryWriteRow;

@RequiredArgsConstructor
public class UnionMapReduceOperation implements Operation {
    private static final int LEFT_POSITION = 0;
    private static final int RIGHT_POSITION = 1;
    private static final String UNION_SCHEMA = "unionSchema";

    private final JobConfigurationCommons jobConfigurationCommons;
    private final ParquetSchemaReader parquetSchemaReader;
    @Getter
    private final String name = UNION;

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
        PhysicalUnion physicalUnion = (PhysicalUnion) invocationParameters.getPhysicalOperation();
        String leftInputPath = getInputPath(invocationParameters, LEFT_POSITION);
        String rightInputPath = getInputPath(invocationParameters, RIGHT_POSITION);
        String outputPath = PATH_PREFIX + physicalUnion.getId();
        Job job = jobConfigurationCommons.createMapperCombinerReducerJobWithBinaryInputs(BinaryOperationJobConfiguration.builder()
                .leftInputPath(leftInputPath)
                .rightInputPath(rightInputPath)
                .outputPath(outputPath)
                .leftInputFormatClass(ExampleInputFormat.class)
                .rightInputFormatClass(ExampleInputFormat.class)
                .outputFormatClass(ExampleOutputFormat.class)
                .mapOutputKeyClass(Text.class)
                .mapOutputValueClass(NullWritable.class)
                .outputKeyClass(LongWritable.class)
                .outputValueClass(Group.class)
                .leftMapperClass(LeftSourceUnionMapper.class)
                .rightMapperClass(RightSourceUnionMapper.class)
                .combinerClass(UnionCombiner.class)
                .reducerClass(UnionReducer.class)
                .build());
        MessageType leftInputSchema = parquetSchemaReader.readSchema(leftInputPath);
        ExampleOutputFormat.setSchema(job, leftInputSchema);
        ExampleOutputFormat.setCompression(job, CompressionCodecName.UNCOMPRESSED);
        job.getConfiguration().set(UNION_SCHEMA, leftInputSchema.toString());
        job.waitForCompletion(VERBOSE);
        return new ReferenceIntermediateDataset(outputPath);
    }

    private String getInputPath(InvocationParameters invocationParameters, int position) {
        return invocationParameters.getInputIntermediateDatasets().get(position)
                .getValue()
                .get()
                .toString();
    }

    private static class LeftSourceUnionMapper extends Mapper<LongWritable, Group, Text, NullWritable> {
        @Override
        public void map(LongWritable key, Group value, Context context) throws IOException, InterruptedException {
            context.write(new Text(value.toString()), NullWritable.get());
        }
    }

    private static class RightSourceUnionMapper extends Mapper<LongWritable, Group, Text, NullWritable> {
        @Override
        public void map(LongWritable key, Group value, Context context) throws IOException, InterruptedException {
            context.write(new Text(value.toString()), NullWritable.get());
        }
    }

    private static class UnionCombiner extends Reducer<Text, NullWritable, Text, NullWritable> {
        @Override
        public void reduce(Text key, Iterable<NullWritable> notUsed, Context context) throws IOException, InterruptedException {
            context.write(key, NullWritable.get());
        }
    }

    private static class UnionReducer extends Reducer<Text, NullWritable, LongWritable, Group> {
        @Override
        public void reduce(Text key, Iterable<NullWritable> notUsed, Context context) {
            String unionSchemaString = context.getConfiguration().get(UNION_SCHEMA);
            MessageType unionSchema = MessageTypeParser.parseMessageType(unionSchemaString);
            Row row = parseRow(key.toString());
            tryWriteRow(row, unionSchema, context);
        }
    }
}
