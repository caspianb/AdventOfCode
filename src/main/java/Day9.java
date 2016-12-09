import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

public class Day9 {

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("src/main/java/input9.txt");
        List<String> input = Files.readAllLines(path).stream()
                .map(StringUtils::stripToEmpty)
                .collect(Collectors.toList());

        String line = input.get(0);

        problem1(line);
        problem2(line);
    }

    public static void problem1(String line) {

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) != '(') {
                result.append(line.charAt(i));
            }
            else {
                int closeParen = line.indexOf(')', i + 1);
                String read = line.substring(i + 1, closeParen);

                String[] vals = read.split("x");
                int nextChars = Integer.valueOf(vals[0]);
                int times = Integer.valueOf(vals[1]);

                i = closeParen + 1;
                String repeat = line.substring(i, i + nextChars);
                for (int z = 0; z < times; z++) {
                    result.append(repeat);
                }

                i += nextChars - 1;
            }

        }

        System.out.println(result.length());

    }

    public static void problem2(String line) {
        long length = decompress(line);

        System.out.println(length);
    }

    public static long decompress(String text) {
        long length = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) != '(') {
                length++;
            }
            else {
                int closeParen = text.indexOf(')', i + 1);
                String read = text.substring(i + 1, closeParen);

                String[] vals = read.split("x");
                int nextChars = Integer.valueOf(vals[0]);
                int times = Integer.valueOf(vals[1]);

                i = closeParen + 1;
                String repeat = text.substring(i, i + nextChars);

                long val = decompress(repeat);
                length += val * times;

                i += nextChars - 1;
            }
        }

        return length;
    }

}
