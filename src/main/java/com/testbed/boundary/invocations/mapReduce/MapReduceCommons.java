package com.testbed.boundary.invocations.mapReduce;

import lombok.Builder;
import lombok.Data;
import org.apache.hadoop.mapreduce.Reducer.Context;
import org.apache.parquet.example.data.Group;
import org.apache.parquet.example.data.simple.SimpleGroup;
import org.apache.parquet.schema.GroupType;
import org.apache.parquet.schema.MessageType;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MapReduceCommons {
    public static void tryWriteRow(Row row, MessageType schema, Context context) {
        try {
            writeRow(row, schema, context);
        } catch (IOException | InterruptedException exception) {
            throw new RuntimeException(exception);
        }
    }

    private static void writeRow(Row row, MessageType schema, Context context) throws IOException, InterruptedException {
        Group group = new SimpleGroup(schema);
        row.getFields().forEach(field -> group.append(field.name, field.value));
        context.write(null, group);
    }

    public static class RowsParser {
        private static final String FIELD_DELIMITER = "\n";
        private static final String TYPE_DELIMITER = ": ";
        private static final int NAME_POSITION = 0;
        private static final int VALUE_POSITION = 1;
        private static final String EMPTY_STRING = "";

        public static Row parseRow(String rowString) {
            return parseRowWithFieldNamePrefix(rowString, EMPTY_STRING);
        }

        public static Row parseRowWithFieldNamePrefix(String rowString, String fieldNamePrefix) {
            String[] fields = rowString.split(FIELD_DELIMITER);
            return new Row(Arrays.stream(fields)
                    .map(field -> field.split(TYPE_DELIMITER))
                    .map(fieldParts -> Field.builder()
                            .name(fieldNamePrefix + fieldParts[NAME_POSITION])
                            .value(fieldParts[VALUE_POSITION])
                            .build())
                    .collect(Collectors.toList()));
        }
    }

    @Data
    public static class Row {
        private final List<Field> fields;
    }

    @Data
    @Builder
    public static class Field {
        private final String name;
        private final String value;
    }
}
