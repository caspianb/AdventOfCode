package problems;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

public class Day16 {

    private static final String input = "00101000101111010";

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("src/main/input/input16.txt");
        List<String> input = Files.readAllLines(path).stream()
                .map(StringUtils::stripToEmpty)
                .collect(Collectors.toList());

        System.out.println("Running Day16...");
        problem(272);
        problem(35651584);
    }

    public static String adjust(String a) {
        String b = StringUtils.reverse(a);
        b = b.replaceAll("0", "2");
        b = b.replaceAll("1", "0");
        b = b.replaceAll("2", "1");
        return a + "0" + b;
    }

    public static String checksum(String a) {
        StringBuilder checksum = new StringBuilder(a.length() / 2);
        for (int i = 0; i < a.length(); i += 2) {
            char ch = a.charAt(i);
            char ch2 = a.charAt(i + 1);
            checksum.append((ch == ch2) ? "1" : "0");
        }
        return checksum.toString();
    }

    public static void problem(int length) {
        String a = input;
        while (a.length() < length) {
            a = adjust(a);
        }
        a = a.substring(0, length);
        String checksum = a;
        while (checksum == a || checksum.length() % 2 == 0) {
            checksum = checksum(checksum);
        }
        System.out.println(checksum);
    }

}
