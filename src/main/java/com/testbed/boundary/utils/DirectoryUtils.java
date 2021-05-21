package com.testbed.boundary.utils;

import lombok.RequiredArgsConstructor;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class DirectoryUtils {
    private static final boolean RECURSIVE_LIST = true;

    private final FileSystem fileSystem;

    public List<String> tryGetFilesInDirectoryByPattern(final String directory, final Pattern pattern) {
        try {
            RemoteIterator<LocatedFileStatus> files = fileSystem.listFiles(new Path(directory), RECURSIVE_LIST);
            Stream<LocatedFileStatus> locatedFileStatusStream = getStream(files);
            return locatedFileStatusStream
                    .map(FileStatus::getPath)
                    .map(Path::toString)
                    .filter(pattern.asPredicate())
                    .collect(Collectors.toList());
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private Stream<LocatedFileStatus> getStream(final RemoteIterator<LocatedFileStatus> files) throws IOException {
        Stream.Builder<LocatedFileStatus> locatedFileStatusStreamBuilder = Stream.builder();
        while (files.hasNext()) {
            locatedFileStatusStreamBuilder.accept(files.next());
        }
        return locatedFileStatusStreamBuilder.build();
    }
}
