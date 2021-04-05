package com.testbed.boundary.deserializers;

import com.google.common.collect.Streams;
import com.testbed.entities.profiles.ColumnProfile;
import com.testbed.entities.profiles.Profile;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("UnstableApiUsage")
public class AvroProfileDeserializer implements Deserializer<Profile> {
    private static final String ROWS_COUNT = "rows_count";
    private static final String IS_UNIQUE = "is_unique";
    private static final String METADATA_FILENAME = "metadata.avro";

    @Override
    public Profile deserialize(final String path) throws RuntimeException {
        Map<String, ColumnProfile> columns = getColumnProfiles(path);
        return new Profile(columns);
    }

    private Map<String, ColumnProfile> getColumnProfiles(final String path) {
        Pattern columnFileNamePattern = Pattern.compile("count_value_stats_([^/])*\\.avro$");
        List<String> columnFilePaths = tryGetFilesInDirectoryByPattern(path, columnFileNamePattern);
        Stream<String> columnNamesStream = getColumnNamesStream(columnFilePaths);
        Stream<ColumnProfile> columnProfilesStream = getColumnProfileStream(columnFilePaths);
        return Streams.zip(columnNamesStream, columnProfilesStream, AbstractMap.SimpleEntry::new)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private List<String> tryGetFilesInDirectoryByPattern(final String directory, final Pattern pattern) {
        try (Stream<Path> paths = Files.walk(Paths.get(directory))) {
            return paths.map(Path::toString)
                    .filter(pattern.asPredicate())
                    .collect(Collectors.toList());
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private Stream<String> getColumnNamesStream(final List<String> columnProfileFileNames) {
        Pattern columnFileNamePattern = Pattern.compile("count_value_stats_(.*)\\.avro");
        return columnProfileFileNames.stream()
                .map(columnFileNamePattern::matcher)
                .filter(Matcher::find)
                .map(matcher -> matcher.group(1))
                .distinct();
    }

    private Stream<ColumnProfile> getColumnProfileStream(final List<String> columnFilePaths) {
        return columnFilePaths.stream()
                .map(columnFilePath -> columnFilePath + '/' + METADATA_FILENAME)
                .map(this::tryGetDataFileReaderFromFileName)
                .map(this::getColumnProfileFromDataFileReader);
    }

    private DataFileReader<GenericRecord> tryGetDataFileReaderFromFileName(final String filename) {
        DatumReader<GenericRecord> datumReader = new GenericDatumReader<>();
        try {
            return new DataFileReader<>(new File(filename), datumReader);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private ColumnProfile getColumnProfileFromDataFileReader(final DataFileReader<GenericRecord> dataFileReader) {
        GenericRecord genericRecord = dataFileReader.next();
        return ColumnProfile.builder()
            .isUnique(Boolean.parseBoolean(genericRecord.get(IS_UNIQUE).toString()))
            .rowsCount(Long.parseLong(genericRecord.get(ROWS_COUNT).toString())).build();
    }
}
