package com.dootax.teste.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IOUtil {

    public static Set<Path> getFilteredPaths(String folder, List<String> extensions) throws IOException {
        try(Stream<Path> stream = Files.walk(Paths.get(folder), 1)) {
            return stream
                    .filter(f -> !Files.isDirectory(f) && extensions.stream().anyMatch(e -> f.toString().endsWith(e)))
                    .collect(Collectors.toSet());
        }
    }

    public static boolean deletarArquivo(Path path) throws IOException {
        return Files.deleteIfExists(path);
    }

}
