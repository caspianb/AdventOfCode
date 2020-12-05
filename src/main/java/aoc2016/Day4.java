package aoc2016;

import util.ResourceReader;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class Day4 {

    public static void main(String[] args) throws IOException {
        List<String> input = ResourceReader.readLines("aoc2016/input4.txt");

        problem1(input);
        problem2(input);
    }

    public static class Chars implements Comparable<Chars> {
        char ch = 0;
        long times = 0;

        Chars(char ch, long times) {
            this.ch = ch;
            this.times = times;
        }

        @Override
        public int compareTo(Chars o) {
            if (this.times == o.times) {
                return Character.compare(ch, o.ch);
            }

            return -Long.compare(this.times, o.times);
        }
    }

    public static void problem1(List<String> input) {
        long sum = 0;

        for (String line : input) {
            String[] values = line.replaceAll("-|\\[|\\]", " ").split(" ");
            int sector = Integer.valueOf(values[values.length - 2]);
            String checksum = values[values.length - 1];

            Map<Character, Chars> totals = Maps.newLinkedHashMap();
            for (int i = 0; i < values.length - 2; i++) {
                String value = values[i];
                for (char ch : value.toCharArray()) {
                    Chars chars = totals.get(ch);
                    if (chars == null) {
                        chars = new Chars(ch, 0);
                        totals.put(ch, chars);
                    }
                    chars.times++;
                }
            }

            List<Chars> chars = Lists.newArrayList(totals.values());
            chars.sort(null);

            boolean valid = true;
            for (int i = 0; i < checksum.length(); i++) {
                Chars ch = chars.get(i);
                if (!checksum.contains(String.valueOf(ch.ch))) {
                    valid = false;
                    break;
                }
            }

            if (valid) sum += sector;

        }

        System.out.println(sum);
    }

    public static void problem2(List<String> input) {

        for (String line : input) {
            String[] values = line.replaceAll("-|\\[|\\]", " ").split(" ");
            int sector = Integer.valueOf(values[values.length - 2]);

            String translatedMessage = "";
            for (int i = 0; i < values.length - 2; i++) {
                String value = values[i];
                for (char ch : value.toCharArray()) {
                    ch = (char) ((ch - 'a' + sector) % 26 + 'a');
                    translatedMessage += ch;
                }
                translatedMessage += " ";
            }
            if (translatedMessage.contains("object"))
                System.out.println(translatedMessage + " - " + sector);
        }

    }
}
