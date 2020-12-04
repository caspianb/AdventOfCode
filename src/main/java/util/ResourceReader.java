package util;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

public class ResourceReader {

    public static String readFile(String filename) {
        if (filename.startsWith("/")) {
            filename = filename.substring(1);
        }

        try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename)) {
            return new String(in.readAllBytes(), Charset.defaultCharset());
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> readLines(String filename) {
        return readFile(filename).lines().collect(Collectors.toList());
    }

    public static List<Long> readLineNumbers(String filename) {
        return readFile(filename).lines()
                .filter(StringUtils::isNotBlank)
                .map(Long::valueOf)
                .collect(Collectors.toList());
    }

    public static List<List<String>> readCsvLines(String filename) {
        return readLines(filename).stream()
                .map(StringUtils::trimToEmpty)
                .filter(StringUtils::isNotBlank)
                .map(line -> Arrays.asList(line.split(",")))
                .collect(Collectors.toList());
    }

    public static List<String> readCsv(String filename) {
        return Arrays.asList(readFile(filename).split(","));
    }

    public static List<Long> readCsvNumbers(String filename) {
        return readCsv(filename).stream()
                .map(StringUtils::trimToEmpty)
                .filter(StringUtils::isNotBlank)
                .map(Long::valueOf)
                .collect(Collectors.toList());
    }
}
