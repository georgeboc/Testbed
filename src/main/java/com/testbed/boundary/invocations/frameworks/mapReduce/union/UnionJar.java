package com.testbed.boundary.invocations.frameworks.mapReduce.union;

import com.testbed.boundary.invocations.frameworks.mapReduce.MapReduceCommons;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.parquet.example.data.Group;
import org.apache.parquet.schema.MessageType;
import org.apache.parquet.schema.MessageTypeParser;

import java.io.IOException;

import static com.testbed.boundary.invocations.frameworks.mapReduce.MapReduceCommons.RowsParser.parseRow;
import static com.testbed.boundary.invocations.frameworks.mapReduce.MapReduceCommons.tryWriteRow;

public class UnionJar {
    private static final String UNION_SCHEMA = "unionSchema";

    public static class LeftSourceUnionMapper extends Mapper<LongWritable, Group, Text, NullWritable> {
        @Override
        public void map(LongWritable key, Group value, Context context) throws IOException, InterruptedException {
            context.write(new Text(value.toString()), NullWritable.get());
        }
    }

    public static class RightSourceUnionMapper extends Mapper<LongWritable, Group, Text, NullWritable> {
        @Override
        public void map(LongWritable key, Group value, Context context) throws IOException, InterruptedException {
            context.write(new Text(value.toString()), NullWritable.get());
        }
    }

    public static class UnionCombiner extends Reducer<Text, NullWritable, Text, NullWritable> {
        @Override
        public void reduce(Text key, Iterable<NullWritable> notUsed, Context context) throws IOException, InterruptedException {
            context.write(key, NullWritable.get());
        }
    }

    public static class UnionReducer extends Reducer<Text, NullWritable, LongWritable, Group> {
        @Override
        public void reduce(Text key, Iterable<NullWritable> notUsed, Context context) {
            String unionSchemaString = context.getConfiguration().get(UNION_SCHEMA);
            MessageType unionSchema = MessageTypeParser.parseMessageType(unionSchemaString);
            MapReduceCommons.Row row = parseRow(key.toString());
            tryWriteRow(row, unionSchema, context);
        }
    }
}
