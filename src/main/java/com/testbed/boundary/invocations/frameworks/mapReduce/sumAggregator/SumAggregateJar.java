package com.testbed.boundary.invocations.frameworks.mapReduce.sumAggregator;

import com.google.common.collect.Streams;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.parquet.example.data.Group;
import org.apache.parquet.example.data.simple.SimpleGroup;
import org.apache.parquet.schema.MessageType;
import org.apache.parquet.schema.PrimitiveType;
import org.apache.parquet.schema.Type;

import java.io.IOException;

public class SumAggregateJar {
    private static final String AGGREGATE_COLUMN_NAME = "aggregateColumnName";
    private static final String SCHEMA_NAME = "aggregationSchema";
    private static final String SUM_PREFIX = "Sum";

    public static class SumAggregateMapper extends Mapper<LongWritable, Group, NullWritable, DoubleWritable> {
        private static final int DEFAULT_POSITION = 0;

        @Override
        public void map(final LongWritable key, final Group value, final Context context) throws IOException, InterruptedException {
            String aggregateColumnName = context.getConfiguration().get(AGGREGATE_COLUMN_NAME);
            double columnValue = Double.parseDouble(value.getString(aggregateColumnName, DEFAULT_POSITION));
            context.write(NullWritable.get(), new DoubleWritable(columnValue));
        }
    }

    public static class SumAggregateCombiner extends Reducer<NullWritable, DoubleWritable, NullWritable, DoubleWritable> {
        private static final double INITIAL_VALUE = 0.0;

        @Override
        public void reduce(final NullWritable key,
                           final Iterable<DoubleWritable> values,
                           final Context context) throws IOException, InterruptedException {
            double aggregateValue = Streams.stream(values).map(DoubleWritable::get).reduce(INITIAL_VALUE, Double::sum);
            context.write(key, new DoubleWritable(aggregateValue));
        }
    }

    public static class SumAggregateReducer extends Reducer<NullWritable, DoubleWritable, LongWritable, Group> {
        private static final double INITIAL_VALUE = 0.0;

        @Override
        public void reduce(final NullWritable notUsed,
                           final Iterable<DoubleWritable> values,
                           final Context context) throws IOException, InterruptedException {
            String aggregateColumnName = context.getConfiguration().get(AGGREGATE_COLUMN_NAME);
            String sumAggregatedColumnName = SUM_PREFIX + aggregateColumnName;
            Group aggregateGroup = createAggregateGroup(sumAggregatedColumnName);
            double aggregateValue = getAggregateValue(values);
            aggregateGroup.append(sumAggregatedColumnName, String.valueOf(aggregateValue));
            context.write(null, aggregateGroup);
        }

        private Double getAggregateValue(final Iterable<DoubleWritable> values) {
            return Streams.stream(values).map(DoubleWritable::get).reduce(INITIAL_VALUE, Double::sum);
        }

        private Group createAggregateGroup(final String sumAggregatedColumnName) {
            Type columnType = new PrimitiveType(Type.Repetition.OPTIONAL,
                    PrimitiveType.PrimitiveTypeName.BINARY,
                    sumAggregatedColumnName);
            MessageType aggregateSchema = new MessageType(SCHEMA_NAME, columnType);
            return new SimpleGroup(aggregateSchema);
        }
    }
}
