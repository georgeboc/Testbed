package boundary.readers;

import com.google.common.collect.Streams;
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
    public String getValueFromRowId(long rowId, String columnName, String directory) {
        Pattern columnPartsPattern = Pattern.compile("count_value_stats_" + columnName + ".avro/part-.*\\.avro$");
        List<String> columnPartsPaths = tryGetFilesInDirectoryByPattern(directory, columnPartsPattern);
        return columnPartsPaths.stream()
                .map(this::tryGetDataFileReaderFromFileName)
                .flatMap(dataFileReader -> Streams.stream((Iterable<GenericRecord>) dataFileReader))
                .limit(rowId)
                .reduce((first, second) -> second)
                .map(genericRecord -> genericRecord.get(VALUE).toString())
                .get();
    }

    private List<String> tryGetFilesInDirectoryByPattern(String directory, Pattern pattern) {
        try (Stream<Path> paths = Files.walk(Paths.get(directory))) {
            return paths.map(Path::toString)
                    .filter(pattern.asPredicate())
                    .collect(Collectors.toList());
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private DataFileReader<GenericRecord> tryGetDataFileReaderFromFileName(String filename) {
        DatumReader<GenericRecord> datumReader = new GenericDatumReader<>();
        try {
            return new DataFileReader<>(new File(filename), datumReader);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
