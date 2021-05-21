package com.testbed.boundary.invocations.frameworks.mapReduce.groupBy;

import lombok.RequiredArgsConstructor;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.parquet.example.data.Group;
import org.apache.parquet.example.data.simple.SimpleGroup;
import org.apache.parquet.schema.GroupType;
import org.apache.parquet.schema.MessageType;
import org.apache.parquet.schema.MessageTypeParser;
import org.apache.parquet.schema.Type;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GroupByJar {
    private static final String GROUP_BY_COLUMN_INDEXES = "groupByColumnIndexes";
    private static final String GROUP_BY_SCHEMA = "groupBySchema";

    public static class GroupByMapper extends Mapper<LongWritable, Group, Text, NullWritable> {
        private static final int DEFAULT_POSITION = 0;
        private static final String TYPE_DELIMITER = ":";
        private static final CharSequence COLUMN_NAME_VALUES_DELIMITER = ",";

        @Override
        public void map(final LongWritable key, final Group value, final Context context) throws IOException,
                InterruptedException {
            int[] groupingColumnIndexes = context.getConfiguration().getInts(GROUP_BY_COLUMN_INDEXES);
            GroupType originalGroupType = value.getType();
            String groupByColumnNameValues = Arrays.stream(groupingColumnIndexes)
                    .mapToObj(originalGroupType::getType)
                    .map(Type::getName)
                    .map(groupingColumnName -> groupingColumnName + TYPE_DELIMITER +
                            value.getString(groupingColumnName, DEFAULT_POSITION))
                    .collect(Collectors.joining(COLUMN_NAME_VALUES_DELIMITER));
            context.write(new Text(groupByColumnNameValues), NullWritable.get());
        }
    }

    public static class GroupByReducer extends Reducer<Text, NullWritable, LongWritable, Group> {
        private static final String TYPE_DELIMITER = ":";
        private static final String COLUMN_NAME_VALUES_DELIMITER = ",";
        private static final int COLUMN_NAME_POSITION = 0;
        private static final int COLUMN_VALUE_POSITION = 1;

        @Override
        public void reduce(final Text key, final Iterable<NullWritable> notUsed, final Context context) throws IOException, InterruptedException {
            Group groupByGroup = createGroupFromSchema(context.getConfiguration().get(GROUP_BY_SCHEMA));
            String[] groupByColumnNameValues = key.toString().split(COLUMN_NAME_VALUES_DELIMITER);
            writeColumnNameValuesToGroup(groupByGroup, groupByColumnNameValues);
            context.write(null, groupByGroup);
        }

        private void writeColumnNameValuesToGroup(final Group groupByGroup, final String[] groupByColumnNameValues) {
            Arrays.stream(groupByColumnNameValues)
                    .map(columnNameValue -> columnNameValue.split(TYPE_DELIMITER))
                    .forEach(columnNameAndValue -> groupByGroup.append(columnNameAndValue[COLUMN_NAME_POSITION],
                            columnNameAndValue[COLUMN_VALUE_POSITION]));
        }

        private Group createGroupFromSchema(final String groupBySchemaString) {
            MessageType groupBySchema = MessageTypeParser.parseMessageType(groupBySchemaString);
            return new SimpleGroup(groupBySchema);
        }
    }
}
