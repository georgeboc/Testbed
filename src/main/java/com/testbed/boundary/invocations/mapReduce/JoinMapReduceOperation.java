package com.testbed.boundary.invocations.mapReduce;

import com.clearspring.analytics.util.Lists;
import com.google.common.collect.Streams;
import com.testbed.boundary.invocations.InvocationParameters;
import com.testbed.boundary.invocations.Operation;
import com.testbed.boundary.invocations.intermediateDatasets.IntermediateDataset;
import com.testbed.boundary.invocations.intermediateDatasets.ReferenceIntermediateDataset;
import com.testbed.boundary.utils.ParquetSchemaReader;
import com.testbed.entities.operations.physical.PhysicalJoin;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.ListUtils;
import org.apache.hadoop.io.LongWritable;
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
import org.apache.parquet.schema.PrimitiveType;
import org.apache.parquet.schema.Type;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.testbed.boundary.invocations.OperationsConstants.JOIN;
import static com.testbed.boundary.invocations.mapReduce.JobConfigurationCommons.PATH_PREFIX;
import static com.testbed.boundary.invocations.mapReduce.JobConfigurationCommons.VERBOSE;
import static com.testbed.boundary.invocations.mapReduce.MapReduceCommons.tryWriteRow;
import static com.testbed.boundary.invocations.mapReduce.MapReduceCommons.Row;
import static com.testbed.boundary.invocations.mapReduce.MapReduceCommons.RowsParser;

@RequiredArgsConstructor
public class JoinMapReduceOperation implements Operation {
    private static final int LEFT_POSITION = 0;
    private static final int RIGHT_POSITION = 1;
    private static final String JOIN_SCHEMA = "joinSchema";
    private static final String JOINED_SCHEMA_NAME = "joinedSchema";
    private static final String LEFT_PREFIX = "Left";
    private static final String RIGHT_PREFIX = "Right";
    private static final String LEFT_SOURCE_JOIN_COLUMN_INDEX = "leftSourceJoinColumnIndex";
    private static final String RIGHT_SOURCE_JOIN_COLUMN_INDEX = "rightSourceJoinColumnIndex";

    private final JobConfigurationCommons jobConfigurationCommons;
    private final ParquetSchemaReader parquetSchemaReader;
    @Getter
    private final String name = JOIN;

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
        PhysicalJoin physicalJoin = (PhysicalJoin) invocationParameters.getPhysicalOperation();
        String leftInputPath = getInputPath(invocationParameters, LEFT_POSITION);
        String rightInputPath = getInputPath(invocationParameters, RIGHT_POSITION);
        String outputPath = PATH_PREFIX + physicalJoin.getId();
        Job job = jobConfigurationCommons.createMapperReducerJobWithBinaryInputs(BinaryOperationJobConfiguration.builder()
                .leftInputPath(leftInputPath)
                .rightInputPath(rightInputPath)
                .outputPath(outputPath)
                .leftInputFormatClass(ExampleInputFormat.class)
                .rightInputFormatClass(ExampleInputFormat.class)
                .outputFormatClass(ExampleOutputFormat.class)
                .mapOutputKeyClass(Text.class)
                .mapOutputValueClass(Text.class)
                .outputKeyClass(LongWritable.class)
                .outputValueClass(Group.class)
                .leftMapperClass(LeftSourceJoinMapper.class)
                .rightMapperClass(RightSourceJoinMapper.class)
                .reducerClass(JoinReducer.class)
                .build());
        MessageType leftInputSchema = parquetSchemaReader.readSchema(leftInputPath);
        MessageType rightInputSchema = parquetSchemaReader.readSchema(rightInputPath);
        MessageType joinSchema = getJoinSchema(leftInputSchema, rightInputSchema);
        ExampleOutputFormat.setSchema(job, joinSchema);
        ExampleOutputFormat.setCompression(job, CompressionCodecName.UNCOMPRESSED);
        int leftSourceJoinColumnIndex = leftInputSchema.getFieldIndex(physicalJoin.getJoinLeftColumnName());
        int rightSourceJoinColumnIndex = rightInputSchema.getFieldIndex(physicalJoin.getJoinRightColumnName());
        job.getConfiguration().setInt(LEFT_SOURCE_JOIN_COLUMN_INDEX, leftSourceJoinColumnIndex);
        job.getConfiguration().setInt(RIGHT_SOURCE_JOIN_COLUMN_INDEX, rightSourceJoinColumnIndex);
        job.getConfiguration().set(JOIN_SCHEMA, joinSchema.toString());
        job.waitForCompletion(VERBOSE);
        return new ReferenceIntermediateDataset(outputPath);
    }

    private String getInputPath(InvocationParameters invocationParameters, int position) {
        return invocationParameters.getInputIntermediateDatasets().get(position)
                .getValue()
                .get()
                .toString();
    }

    private MessageType getJoinSchema(MessageType leftSchema, MessageType rightSchema) {
        List<Type> leftSchemaFields = getSchemaFields(leftSchema, LEFT_PREFIX);
        List<Type> rightSchemaFields = getSchemaFields(rightSchema, RIGHT_PREFIX);
        List<Type> joinedFields = Streams.concat(leftSchemaFields.stream(), rightSchemaFields.stream())
                .collect(Collectors.toList());
        return new MessageType(JOINED_SCHEMA_NAME, joinedFields);
    }

    private List<Type> getSchemaFields(MessageType leftSchema, String fieldNamePrefix) {
        return leftSchema.getFields().stream()
                .map(field -> new PrimitiveType(field.getRepetition(),
                        field.asPrimitiveType().getPrimitiveTypeName(),
                        fieldNamePrefix + field.getName()))
                .collect(Collectors.toList());
    }

    private static class LeftSourceJoinMapper extends Mapper<LongWritable, Group, Text, Text> {
        private static final int DEFAULT_POSITION = 0;
        private static final int DEFAULT_VALUE = 0;
        private static final String MAPPER_TYPE_AND_ROW_DELIMITER = ",";
        private static final String LEFT_MAPPER = "leftMapper";

        @Override
        public void map(LongWritable key, Group value, Context context) throws IOException, InterruptedException {
            int leftSourceJoinColumnIndex = context.getConfiguration().getInt(LEFT_SOURCE_JOIN_COLUMN_INDEX, DEFAULT_VALUE);
            String rowValue = value.getString(leftSourceJoinColumnIndex, DEFAULT_POSITION);
            context.write(new Text(rowValue), new Text(LEFT_MAPPER + MAPPER_TYPE_AND_ROW_DELIMITER + value));
        }
    }

    private static class RightSourceJoinMapper extends Mapper<LongWritable, Group, Text, Text> {
        private static final int DEFAULT_POSITION = 0;
        private static final int DEFAULT_VALUE = 0;
        private static final String MAPPER_TYPE_AND_ROW_DELIMITER = ",";
        private static final String RIGHT_MAPPER = "rightMapper";

        @Override
        public void map(LongWritable key, Group value, Context context) throws IOException, InterruptedException {
            int rightSourceJoinColumnIndex = context.getConfiguration().getInt(RIGHT_SOURCE_JOIN_COLUMN_INDEX, DEFAULT_VALUE);
            String rowValue = value.getString(rightSourceJoinColumnIndex, DEFAULT_POSITION);
            context.write(new Text(rowValue), new Text(RIGHT_MAPPER + MAPPER_TYPE_AND_ROW_DELIMITER + value));
        }
    }

    private static class JoinReducer extends Reducer<Text, Text, LongWritable, Group> {
        private static final String LEFT_MAPPER = "leftMapper";
        private static final String MAPPER_TYPE_AND_ROW_DELIMITER = ",";
        private static final int MAPPER_TYPE_POSITION = 0;
        private static final int ROW_POSITION = 1;
        private static final String LEFT_PREFIX = "Left";
        private static final String RIGHT_PREFIX = "Right";

        @Override
        public void reduce(Text key, Iterable<Text> values, Context context) {
            String joinSchemaString = context.getConfiguration().get(JOIN_SCHEMA);
            MessageType joinSchema = MessageTypeParser.parseMessageType(joinSchemaString);
            doJoin(values, joinSchema, context);
        }

        private void doJoin(Iterable<Text> rows, MessageType joinSchema, Context context) {
            LeftRightRows leftRightRows = getLeftRightRows(rows);
            Stream<Row> crossProductStream = getCrossProductStream(leftRightRows);
            crossProductStream.forEach(row -> tryWriteRow(row, joinSchema, context));
        }

        private LeftRightRows getLeftRightRows(Iterable<Text> rows) {
            List<Row> leftRows = Lists.newArrayList();
            List<Row> rightRows = Lists.newArrayList();
            Streams.stream(rows)
                .map(mapperTypeAndRow -> mapperTypeAndRow.toString().split(MAPPER_TYPE_AND_ROW_DELIMITER))
                .forEach(mapperTypeAndRow -> classifyByMapperType(mapperTypeAndRow, leftRows, rightRows));
            return LeftRightRows.builder()
                    .leftRows(leftRows)
                    .rightRows(rightRows)
                    .build();
        }

        private void classifyByMapperType(String[] mapperTypeAndRows, List<Row> leftRows, List<Row> rightRows) {
            if (mapperTypeAndRows[MAPPER_TYPE_POSITION].equals(LEFT_MAPPER)) {
                leftRows.add(RowsParser.parseRowWithFieldNamePrefix(mapperTypeAndRows[ROW_POSITION], LEFT_PREFIX));
            } else {
                rightRows.add(RowsParser.parseRowWithFieldNamePrefix(mapperTypeAndRows[ROW_POSITION], RIGHT_PREFIX));
            }
        }

        private Stream<Row> getCrossProductStream(LeftRightRows leftRightRows) {
            return leftRightRows.getLeftRows().stream()
                    .flatMap(leftRow -> leftRightRows.getRightRows().stream()
                        .map(rightRow -> uniteFields(leftRow, rightRow)));
        }

        private Row uniteFields(Row leftRow, Row rightRow) {
            return new Row(ListUtils.union(leftRow.getFields(), rightRow.getFields()));
        }

        @Data
        @Builder
        private static class LeftRightRows {
            private final List<MapReduceCommons.Row> leftRows;
            private final List<MapReduceCommons.Row> rightRows;
        }
    }
}
