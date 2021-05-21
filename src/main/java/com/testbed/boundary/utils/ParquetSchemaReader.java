package com.testbed.boundary.utils;

import lombok.RequiredArgsConstructor;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.hadoop.ParquetFileReader;
import org.apache.parquet.hadoop.metadata.ParquetMetadata;
import org.apache.parquet.schema.MessageType;

import java.util.regex.Pattern;

@RequiredArgsConstructor
public class ParquetSchemaReader {
    private static final int FIRST = 0;

    private final Configuration configuration;
    private final DirectoryUtils directoryUtils;

    public MessageType readSchema(final String inputPath) {
        Path parquetFilePath = new Path(directoryUtils.tryGetFilesInDirectoryByPattern(inputPath,
                Pattern.compile(".*part-.*parquet$")).get(FIRST));
        ParquetMetadata readFooter = tryReadFooter(parquetFilePath);
        return readFooter.getFileMetaData().getSchema();
    }

    private ParquetMetadata tryReadFooter(final Path parquetFilePath) {
        try {
            return ParquetFileReader.readFooter(configuration, parquetFilePath);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
