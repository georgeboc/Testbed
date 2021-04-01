package com.testbed.boundary.readers;

import com.google.common.collect.Streams;
import com.testbed.entities.profiles.ColumnProfile;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AvroColumnReader implements ColumnReader {

    private static final String VALUE = "Value";

    @Override
    public String getValueFromSelectivityFactor(final double selectivityFactor,
                                                final ColumnProfile columnProfile,
                                                final String columnName,
                                                final String directory) {
        List<String> columnPartsPaths = tryGetFilesInDirectoryByPattern(directory, columnName);
        int columnPartsCount = columnPartsPaths.size();
        int columnPartId = (int) (columnPartsCount*selectivityFactor);
        String columnPartPath = columnPartsPaths.get(columnPartId);
        DataFileReader<GenericRecord> dataFileReader = tryGetDataFileReaderFromFileName(columnPartPath);
        long rowId = getRowId(selectivityFactor, columnProfile, columnPartsCount, columnPartId);
        return getGenericRecordByRowId(dataFileReader, rowId).get(VALUE).toString();
    }

    private List<String> tryGetFilesInDirectoryByPattern(final String directory, final String columnName) {
        Pattern columnPartsPattern = Pattern.compile("count_value_stats_" + columnName + ".avro/part-.*\\.avro$");
        try (Stream<Path> paths = Files.walk(Paths.get(directory))) {
            return paths.map(Path::toString)
                    .filter(columnPartsPattern.asPredicate())
                    .sorted()
                    .collect(Collectors.toList());
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private DataFileReader<GenericRecord> tryGetDataFileReaderFromFileName(final String filename) {
        final DatumReader<GenericRecord> datumReader = new GenericDatumReader<>();
        try {
            return new DataFileReader<>(new File(filename), datumReader);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private long getRowId(final double selectivityFactor,
                          final ColumnProfile columnProfile,
                          final int columnPartsCount,
                          final double columnPartId) {
        long rowsPerColumnPart = columnProfile.getRowsCount()/columnPartsCount;
        double deltaSelectivityFactor = selectivityFactor - columnPartId/columnPartsCount;
        long rowId = (long) (rowsPerColumnPart * deltaSelectivityFactor);
        if (rowId == rowsPerColumnPart) {
            return rowId - 1;
        }
        return rowId;
    }

    private GenericRecord getGenericRecordByRowId(final Iterable<GenericRecord> dataFileReader,
                                                  final long accessibleRowId) {
        return Streams.stream(dataFileReader)
                .skip(accessibleRowId)
                .findFirst()
                .get();
    }
}
