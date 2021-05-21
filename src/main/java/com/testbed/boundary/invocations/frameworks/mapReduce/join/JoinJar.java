package com.testbed.boundary.invocations.frameworks.mapReduce.join;

import com.clearspring.analytics.util.Lists;
import com.google.common.collect.Streams;
import com.testbed.boundary.invocations.frameworks.mapReduce.MapReduceCommons;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.collections.ListUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.parquet.example.data.Group;
import org.apache.parquet.schema.MessageType;
import org.apache.parquet.schema.MessageTypeParser;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import static com.testbed.boundary.invocations.frameworks.mapReduce.MapReduceCommons.RowsParser.addPrefixToRowsFieldName;
import static com.testbed.boundary.invocations.frameworks.mapReduce.MapReduceCommons.RowsParser.parseRow;
import static com.testbed.boundary.invocations.frameworks.mapReduce.MapReduceCommons.tryWriteRow;

public class JoinJar {
    private static final String JOIN_SCHEMA = "joinSchema";
    private static final String LEFT_SOURCE_JOIN_COLUMN_INDEX = "leftSourceJoinColumnIndex";
    private static final String RIGHT_SOURCE_JOIN_COLUMN_INDEX = "rightSourceJoinColumnIndex";

    public static class LeftSourceJoinMapper extends Mapper<LongWritable, Group, Text, Text> {
        private static final int DEFAULT_POSITION = 0;
        private static final int DEFAULT_VALUE = 0;
        private static final String MAPPER_TYPE_AND_ROW_DELIMITER = ",";
        private static final String LEFT_MAPPER = "leftMapper";

        @Override
        public void map(final LongWritable key, final Group value, final Context context) throws IOException, InterruptedException {
            int leftSourceJoinColumnIndex = context.getConfiguration().getInt(LEFT_SOURCE_JOIN_COLUMN_INDEX, DEFAULT_VALUE);
            String rowValue = value.getString(leftSourceJoinColumnIndex, DEFAULT_POSITION);
            context.write(new Text(rowValue), new Text(LEFT_MAPPER + MAPPER_TYPE_AND_ROW_DELIMITER + value));
        }
    }

    public static class RightSourceJoinMapper extends Mapper<LongWritable, Group, Text, Text> {
        private static final int DEFAULT_POSITION = 0;
        private static final int DEFAULT_VALUE = 0;
        private static final String MAPPER_TYPE_AND_ROW_DELIMITER = ",";
        private static final String RIGHT_MAPPER = "rightMapper";

        @Override
        public void map(final LongWritable key, final Group value, final Context context) throws IOException, InterruptedException {
            int rightSourceJoinColumnIndex = context.getConfiguration().getInt(RIGHT_SOURCE_JOIN_COLUMN_INDEX, DEFAULT_VALUE);
            String rowValue = value.getString(rightSourceJoinColumnIndex, DEFAULT_POSITION);
            context.write(new Text(rowValue), new Text(RIGHT_MAPPER + MAPPER_TYPE_AND_ROW_DELIMITER + value));
        }
    }

    public static class JoinReducer extends Reducer<Text, Text, LongWritable, Group> {
        private static final String LEFT_MAPPER = "leftMapper";
        private static final String MAPPER_TYPE_AND_ROW_DELIMITER = ",";
        private static final int MAPPER_TYPE_POSITION = 0;
        private static final int ROW_POSITION = 1;
        private static final String LEFT_PREFIX = "Left";
        private static final String RIGHT_PREFIX = "Right";

        @Override
        public void reduce(final Text key, final Iterable<Text> values, final Context context) {
            String joinSchemaString = context.getConfiguration().get(JOIN_SCHEMA);
            MessageType joinSchema = MessageTypeParser.parseMessageType(joinSchemaString);
            doJoin(values, joinSchema, context);
        }

        private void doJoin(final Iterable<Text> rows, final MessageType joinSchema, final Context context) {
            LeftRightRows leftRightRows = getLeftRightRows(rows);
            Stream<MapReduceCommons.Row> crossProductStream = getCrossProductStream(leftRightRows);
            crossProductStream.forEach(row -> tryWriteRow(row, joinSchema, context));
        }

        private LeftRightRows getLeftRightRows(final Iterable<Text> rows) {
            List<MapReduceCommons.Row> leftRows = Lists.newArrayList();
            List<MapReduceCommons.Row> rightRows = Lists.newArrayList();
            Streams.stream(rows)
                    .map(mapperTypeAndRow -> mapperTypeAndRow.toString().split(MAPPER_TYPE_AND_ROW_DELIMITER))
                    .forEach(mapperTypeAndRow -> classifyByMapperType(mapperTypeAndRow, leftRows, rightRows));
            return LeftRightRows.builder()
                    .leftRows(leftRows)
                    .rightRows(rightRows)
                    .build();
        }

        private void classifyByMapperType(final String[] mapperTypeAndRows,
                                          final List<MapReduceCommons.Row> leftRows,
                                          final List<MapReduceCommons.Row> rightRows) {
            if (mapperTypeAndRows[MAPPER_TYPE_POSITION].equals(LEFT_MAPPER)) {
                leftRows.add(addPrefixToRowsFieldName(parseRow(mapperTypeAndRows[ROW_POSITION]), LEFT_PREFIX));
            } else {
                rightRows.add(addPrefixToRowsFieldName(parseRow(mapperTypeAndRows[ROW_POSITION]), RIGHT_PREFIX));
            }
        }

        private Stream<MapReduceCommons.Row> getCrossProductStream(final LeftRightRows leftRightRows) {
            return leftRightRows.getLeftRows().stream()
                    .flatMap(leftRow -> leftRightRows.getRightRows().stream()
                            .map(rightRow -> uniteFields(leftRow, rightRow)));
        }

        private MapReduceCommons.Row uniteFields(final MapReduceCommons.Row leftRow, final MapReduceCommons.Row rightRow) {
            return new MapReduceCommons.Row(ListUtils.union(leftRow.getFields(), rightRow.getFields()));
        }

        @Data
        @Builder
        private static class LeftRightRows {
            private final List<MapReduceCommons.Row> leftRows;
            private final List<MapReduceCommons.Row> rightRows;
        }
    }
}
