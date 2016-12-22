package problems;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

public class Day21 {

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("src/main/input/input21.txt");
        List<String> instructions = Files.readAllLines(path).stream()
                .map(StringUtils::stripToEmpty)
                .collect(Collectors.toList());

        final String input1 = "abcdefgh";
        System.out.println(solve(input1, instructions, false));

        final String input2 = "fbgdceah";
        System.out.println(solve(input2, instructions, true));
    }

    public static StringBuilder swap(StringBuilder input, String[] args) {
        int pos1 = 0;
        int pos2 = 0;
        char ch1 = 0;
        char ch2 = 0;

        if (args[1].equals("position")) {
            pos1 = Integer.valueOf(args[2]);
            pos2 = Integer.valueOf(args[5]);
            ch1 = input.charAt(pos1);
            ch2 = input.charAt(pos2);
        }
        else if (args[1].equals("letter")) {
            ch1 = args[2].charAt(0);
            ch2 = args[5].charAt(0);
            pos1 = input.indexOf(args[2]);
            pos2 = input.indexOf(args[5]);
        }

        input.setCharAt(pos1, ch2);
        input.setCharAt(pos2, ch1);

        return input;
    }

    public static StringBuilder rotate(StringBuilder input, String[] args) {
        int shift = 0;

        if (args[1].equals("left")) {
            shift = -Integer.valueOf(args[2]);
        }
        else if (args[1].equals("right")) {
            shift = Integer.valueOf(args[2]);
        }
        else if (args[1].equals("based")) {
            shift = input.indexOf(args[6]);
            if (shift >= 4) shift += 1;
            shift += 1;
        }

        String rotate = StringUtils.rotate(input.toString(), shift);
        input.replace(0, input.length(), rotate);

        return input;
    }

    public static StringBuilder rotateReversed(StringBuilder input, String[] args) {
        int shift = 0;

        if (args[1].equals("left")) {
            shift = Integer.valueOf(args[2]);
        }
        else if (args[1].equals("right")) {
            shift = -Integer.valueOf(args[2]);
        }
        else if (args[1].equals("based")) {
            // try all non-reversed rotations until we find one that yields in our current state...
            for (int i = 0; i < input.length(); i++) {
                StringBuilder rotate = new StringBuilder(StringUtils.rotate(input.toString(), i));
                if (rotate(rotate, args).toString().equals(input.toString())) {
                    input = new StringBuilder(StringUtils.rotate(input.toString(), i));
                    break;
                }
            }
        }

        if (shift != 0) {
            String rotate = StringUtils.rotate(input.toString(), shift);
            input.replace(0, input.length(), rotate);
        }

        return input;
    }

    public static StringBuilder reverse(StringBuilder input, String[] args) {
        int pos1 = Integer.valueOf(args[2]);
        int pos2 = Integer.valueOf(args[4]);

        String reverse = StringUtils.reverse(input.substring(pos1, pos2 + 1));
        input.replace(pos1, pos2 + 1, reverse);

        return input;
    }

    public static StringBuilder move(StringBuilder input, String[] args) {
        int pos1 = Integer.valueOf(args[2]);
        int pos2 = Integer.valueOf(args[5]);

        char ch = input.charAt(pos1);
        input.replace(pos1, pos1 + 1, "");
        input.insert(pos2, ch);

        return input;
    }

    public static StringBuilder moveReversed(StringBuilder input, String[] args) {
        int pos1 = Integer.valueOf(args[2]);
        int pos2 = Integer.valueOf(args[5]);

        char ch = input.charAt(pos2);
        input.replace(pos2, pos2 + 1, "");
        input.insert(pos1, ch);

        return input;
    }

    public static String solve(String input, List<String> instructions, boolean reverse) {
        StringBuilder result = new StringBuilder(input);

        if (reverse) {
            instructions = Lists.reverse(instructions);
        }

        for (String line : instructions) {
            String[] args = line.split(" ");
            switch (args[0]) {
                case "swap":
                    result = swap(result, args);
                    break;
                case "rotate":
                    result = (!reverse) ? rotate(result, args) : rotateReversed(result, args);
                    break;
                case "reverse":
                    result = reverse(result, args);
                    break;
                case "move":
                    result = (!reverse) ? move(result, args) : moveReversed(result, args);
                    break;
            }
        }

        return result.toString();
    }

}
