package com.testbed.boundary.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DirectoryUtils {
    public static List<String> tryGetFilesInDirectoryByPattern(final String directory, final Pattern pattern) {
        try (Stream<Path> paths = Files.walk(Paths.get(directory))) {
            return paths.map(Path::toString)
                    .filter(pattern.asPredicate())
                    .collect(Collectors.toList());
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
