import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.util.concurrent.AtomicLongMap;

public class Day6 {

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("src/main/java/input6.txt");
        List<String> input = Files.readAllLines(path).stream()
                .map(StringUtils::stripToEmpty)
                .collect(Collectors.toList());

        System.out.println("Running 1...");
        problem1(input);
        problem2(input);
    }

    public static void problem1(List<String> input) {
        ListMultimap<Integer, Character> totals = ArrayListMultimap.create();

        for (String line : input) {
            int pos = 0;
            for (char ch : line.toCharArray()) {
                totals.put(pos++, ch);
            }
        }

        System.out.println(totals);

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

    public static void problem2(List<String> input) {
        ListMultimap<Integer, Character> totals = ArrayListMultimap.create();

        for (String line : input) {
            int pos = 0;
            for (char ch : line.toCharArray()) {
                totals.put(pos++, ch);
            }
        }

        System.out.println(totals);

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
}
