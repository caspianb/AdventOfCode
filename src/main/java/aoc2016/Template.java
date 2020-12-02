package aoc2016;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

public class Template {

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("src/main/resources/aoc2016/TEMPLATE.txt");
        List<String> input = Files.readAllLines(path).stream()
                .map(StringUtils::stripToEmpty)
                .collect(Collectors.toList());

        problem1();
        problem2();
    }

    public static void problem1() {

    }

    public static void problem2() {

    }

}
