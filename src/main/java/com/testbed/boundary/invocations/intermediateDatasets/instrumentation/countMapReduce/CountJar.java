package com.testbed.boundary.invocations.intermediateDatasets.instrumentation.countMapReduce;

import com.google.common.collect.Streams;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.parquet.example.data.Group;

import java.io.IOException;

public class CountJar {
    public static class CountMapper extends Mapper<LongWritable, Group, NullWritable, LongWritable> {
        @Override
        public void map(final LongWritable key,
                        final Group value,
                        final Context context) throws IOException, InterruptedException {
            context.write(NullWritable.get(), new LongWritable(1L));
        }
    }

    public static class CountReducer extends Reducer<NullWritable, LongWritable, NullWritable, LongWritable> {
        @Override
        public void reduce(final NullWritable nullWritable,
                           final Iterable<LongWritable> iterable,
                           final Context context) throws IOException, InterruptedException {
            long sum = Streams.stream(iterable).map(LongWritable::get).reduce(Long::sum).orElse(0L);
            context.write(nullWritable, new LongWritable(sum));
        }
    }
}
