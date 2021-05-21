package com.testbed.boundary.invocations.frameworks.mapReduce.select;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.parquet.example.data.Group;

import java.io.IOException;

public class SelectJar {
    private static final String LESS_THAN_OR_EQUAL_VALUE = "lessThanOrEqualValue";
    private static final String COLUMN_INDEX = "columnIndex";

    public static class SelectMapper extends Mapper<LongWritable, Group, LongWritable, Group> {
        private static final int DEFAULT_POSITION = 0;

        @Override
        public void map(final LongWritable key, final Group value, final Context context) throws IOException, InterruptedException {
            int columnIndex = context.getConfiguration().getInt(COLUMN_INDEX, DEFAULT_POSITION);
            String columnValue = value.getValueToString(columnIndex, DEFAULT_POSITION);
            String lessThanOrEqualValue = context.getConfiguration().get(LESS_THAN_OR_EQUAL_VALUE);
            if (columnValue.compareTo(lessThanOrEqualValue) <= 0) {
                context.write(key, value);
            }
        }
    }
}
