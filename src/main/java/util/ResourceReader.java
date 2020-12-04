package util;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

public class ResourceReader {

    /**
     * Reads entire file as a single string.
     * All carriage returns (\r) will be removed to normalize file to linux newline format.
     */
    public static String readFile(String filename) {
        if (filename.startsWith("/")) {
            filename = filename.substring(1);
        }

        try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename)) {
            return new String(in.readAllBytes(), Charset.defaultCharset())
                    .replaceAll("\r", "");
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Reads all lines in a file.
     */
    public static List<String> readLines(String filename) {
        return readFile(filename).lines().collect(Collectors.toList());
    }

    /**
     * Reads in a list of CSV rows building a 2D numeric array.
     */
    public static List<Long> readLineNumbers(String filename) {
        return readFile(filename).lines()
                .filter(StringUtils::isNotBlank)
                .map(Long::valueOf)
                .collect(Collectors.toList());
    }

    /**
     * Reads in a list of CSV rows building a 2D array.
     */
    public static List<List<String>> readCsvLines(String filename) {
        return readLines(filename).stream()
                .map(StringUtils::trimToEmpty)
                .filter(StringUtils::isNotBlank)
                .map(line -> Arrays.asList(line.split(",")))
                .collect(Collectors.toList());
    }

    /**
     * Read in all strings in a file as comma separated values.
     */
    public static List<String> readCsv(String filename) {
        return Arrays.asList(readFile(filename).split(","));
    }

    /**
     * Read in all numbers in a file as comma separated values.
     */
    public static List<Long> readCsvNumbers(String filename) {
        return readCsv(filename).stream()
                .map(StringUtils::trimToEmpty)
                .filter(StringUtils::isNotBlank)
                .map(Long::valueOf)
                .collect(Collectors.toList());
    }

    /**
     * Reads all (potentially) multi-line data elements separated by a blank line.
     * If joinLines is false, then the list will simply return strings of grouped lines.
     * If joinLines is true, then newlines in the data element will be replaced by a space.
     *<br />
     * Example: This will read in the following as two elements:
     * x:123 z:521<br />
     * y:999<br />
     *<br />
     * x:232 y:392 z:201<br />
     * <br/>
     *  - if joinLines == false, the first element will contain a new line<br/>
     *  - if joinLines == true, the first element's new line will be replaced by a space
     */
    public static List<String> readBatchedLines(String filename, boolean joinLines) {
        return Arrays.stream(readFile(filename)
                .split("\n\n"))
                .map(line -> joinLines ? line.replaceAll("\n", " ").trim() : line.trim())
                .collect(Collectors.toList());
    }
}
