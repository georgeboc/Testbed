package com.testbed.boundary.deserializers;

import com.google.common.collect.Streams;
import com.testbed.boundary.utils.DirectoryUtils;
import com.testbed.entities.profiles.ColumnProfile;
import com.testbed.entities.profiles.Profile;
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
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("UnstableApiUsage")
@RequiredArgsConstructor
public class  AvroProfileDeserializer implements Deserializer<Profile> {
    private static final String DISTINCT_ROWS_COUNT = "distinct_rows_count";
    private static final String TOTAL_ROWS_COUNT = "total_rows_count";
    private static final String IS_UNIQUE = "is_unique";

    private final Configuration configuration;
    private final DirectoryUtils directoryUtils;

    @Override
    public Profile deserialize(final String path) throws RuntimeException {
        Map<String, ColumnProfile> columns = getColumnProfiles(path);
        return new Profile(columns);
    }

    private Map<String, ColumnProfile> getColumnProfiles(final String path) {
        Pattern metadataPattern = Pattern.compile(".*metadata\\.avro$");
        List<String> columnMetadataPaths = directoryUtils.tryGetFilesInDirectoryByPattern(path, metadataPattern);
        Stream<String> columnNamesStream = getColumnNamesStream(columnMetadataPaths);
        Stream<ColumnProfile> columnProfilesStream = getColumnProfileStream(columnMetadataPaths);
        return Streams.zip(columnNamesStream, columnProfilesStream, AbstractMap.SimpleEntry::new)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Stream<String> getColumnNamesStream(final List<String> columnMetadataPaths) {
        Pattern columnFileNamePattern = Pattern.compile("count_value_stats_(.*)\\.avro/");
        return columnMetadataPaths.stream()
                .map(columnFileNamePattern::matcher)
                .filter(Matcher::find)
                .map(matcher -> matcher.group(1))
                .distinct();
    }

    private Stream<ColumnProfile> getColumnProfileStream(final List<String> columnMetadataPaths) {
        return columnMetadataPaths.stream()
                .map(this::tryGetDataFileReaderFromFileName)
                .map(this::getColumnProfileFromDataFileReader);
    }

    private FileReader<GenericRecord> tryGetDataFileReaderFromFileName(final String filename) {
        DatumReader<GenericRecord> datumReader = new GenericDatumReader<>();
        try {
            SeekableInput input = new FsInput(new Path(filename), configuration);
            return DataFileReader.openReader(input, datumReader);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private ColumnProfile getColumnProfileFromDataFileReader(final FileReader<GenericRecord> dataFileReader) {
        GenericRecord genericRecord = dataFileReader.next();
        return ColumnProfile.builder()
                .isUnique(Boolean.parseBoolean(genericRecord.get(IS_UNIQUE).toString()))
                .distinctRowsCount(Long.parseLong(genericRecord.get(DISTINCT_ROWS_COUNT).toString()))
                .totalRowsCount(Long.parseLong(genericRecord.get(TOTAL_ROWS_COUNT).toString()))
                .build();
    }
}
