package com.testbed.boundary.readers;

import com.google.common.collect.Streams;
import com.testbed.boundary.utils.DirectoryUtils;
import lombok.RequiredArgsConstructor;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.FileReader;
import org.apache.avro.file.SeekableInput;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.mapred.FsInput;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.Math.max;
import static java.lang.Math.min;

@RequiredArgsConstructor
public class AvroColumnReader implements ColumnReader {
    private static final String VALUE = "Value";

    private final Configuration configuration;
    private final DirectoryUtils directoryUtils;

    @Override
    public String getValueFromSelectivityFactor(final double selectivityFactor,
                                                final long columnDistinctRowsCount,
                                                final String columnName,
                                                final String directory) {
        Pattern columnPartsPattern = Pattern.compile("count_value_stats_" + columnName + "/part-.*\\.avro$");
        List<String> columnPartsPaths = directoryUtils.tryGetFilesInDirectoryByPattern(directory, columnPartsPattern);
        List<String> sortedColumnPartsPaths = columnPartsPaths.stream().sorted().collect(Collectors.toList());
        int sortedColumnPartsCount = sortedColumnPartsPaths.size();
        int sortedColumnPartId = (int) (sortedColumnPartsCount*selectivityFactor);
        String sortedColumnPartPath = sortedColumnPartsPaths.get(max(min(sortedColumnPartId, sortedColumnPartsPaths.size() - 1), 0));
        FileReader<GenericRecord> dataFileReader = tryGetDataFileReaderFromFileName(sortedColumnPartPath);
        long rowId = getRowId(selectivityFactor, columnDistinctRowsCount, sortedColumnPartsCount, sortedColumnPartId);
        return getGenericRecordByRowId(dataFileReader, rowId).get(VALUE).toString();
    }

    private FileReader<GenericRecord> tryGetDataFileReaderFromFileName(final String filename) {
        final DatumReader<GenericRecord> datumReader = new GenericDatumReader<>();
        try {
            SeekableInput input = new FsInput(new Path(filename), configuration);
            return DataFileReader.openReader(input, datumReader);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private long getRowId(final double selectivityFactor,
                          final long columnDistinctRowsCount,
                          final int columnPartsCount,
                          final double columnPartId) {
        long rowsPerColumnPart = columnDistinctRowsCount/columnPartsCount;
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
