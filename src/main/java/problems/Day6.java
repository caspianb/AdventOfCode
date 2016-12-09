package problems;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.util.concurrent.AtomicLongMap;

public class Day6 {

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("src/main/input/input6.txt");
        List<String> input = Files.readAllLines(path).stream()
                .map(StringUtils::stripToEmpty)
                .collect(Collectors.toList());

        problem1(input);
        problem1_streams(input);
        problem2(input);
        problem2_streams(input);
    }

    public static void problem1(List<String> input) {
        ListMultimap<Integer, Character> totals = ArrayListMultimap.create();

        for (String line : input) {
            int pos = 0;
            for (char ch : line.toCharArray()) {
                totals.put(pos++, ch);
            }
        }

        String message = "";
        for (int pos : totals.keySet()) {
            List<Character> list = totals.get(pos);

            AtomicLongMap<Character> hash = AtomicLongMap.create();
            long most = 0;
            char letter = 0;
            for (char ch : list) {
                long value = hash.incrementAndGet(ch);
                if (value > most) {
                    letter = ch;
                    most = value;
                }
            }

            message += letter;
        }

        System.out.println(message);
    }

    public static void problem1_streams(List<String> input) {

        IntStream.range(0, 8).forEach(index -> {
            Map<Character, Long> totals = input.stream()
                    .map(s -> s.charAt(index))
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            char ch = totals.entrySet().stream()
                    .max((e1, e2) -> Long.compare(e1.getValue(), e2.getValue()))
                    .map(Entry::getKey)
                    .get();

            System.out.print(ch);
        });

        System.out.println();
    }

    public static void problem2(List<String> input) {
        ListMultimap<Integer, Character> totals = ArrayListMultimap.create();

        for (String line : input) {
            int pos = 0;
            for (char ch : line.toCharArray()) {
                totals.put(pos++, ch);
            }
        }

        String message = "";
        for (int pos : totals.keySet()) {
            List<Character> list = totals.get(pos);

            AtomicLongMap<Character> hash = AtomicLongMap.create();
            for (char ch : list) {
                hash.incrementAndGet(ch);
            }

            long most = Long.MAX_VALUE;
            char letter = 0;
            for (char ch : list) {
                if (hash.get(ch) < most) {
                    most = hash.get(ch);
                    letter = ch;
                }
            }

            message += letter;
        }

        System.out.println(message);
    }

    public static void problem2_streams(List<String> input) {
        IntStream.range(0, 8).forEach(index -> {
            Map<Character, Long> totals = input.stream()
                    .map(s -> s.charAt(index))
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            char ch = totals.entrySet().stream()
                    .min((e1, e2) -> Long.compare(e1.getValue(), e2.getValue()))
                    .map(Entry::getKey)
                    .get();

            System.out.print(ch);
        });
        System.out.println();
    }
}
