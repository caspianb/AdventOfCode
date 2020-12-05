package aoc2016;

import util.ResourceReader;

import java.io.IOException;
import java.util.List;

public class Day9 {

    public static void main(String[] args) throws IOException {
        List<String> input = ResourceReader.readLines("aoc2016/input9.txt");

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
                int closeParen = text.indexOf(')', i);
                if (closeParen < 0) {
                    length += text.length() - i;
                    break;
                }

                String read = text.substring(i + 1, closeParen);

                String[] vals = read.split("x");
                int nextChars = Integer.valueOf(vals[0]);
                int times = Integer.valueOf(vals[1]);

                i = closeParen + 1;

                String repeat = "";
                if (i + nextChars <= text.length()) {
                    repeat = text.substring(i, i + nextChars);
                }

                long val = decompress(repeat);
                length += val * times;

                i += nextChars - 1;
            }
        }

        return length;
    }

}
