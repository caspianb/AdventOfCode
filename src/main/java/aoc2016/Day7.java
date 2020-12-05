package aoc2016;

import util.ResourceReader;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.common.collect.Sets;

public class Day7 {

    public static void main(String[] args) throws IOException {
        List<String> input = ResourceReader.readLines("aoc2016/input7.txt");

        problem1(input);
        problem2(input);
    }

    public static boolean patternFound(String str) {
        for (int i = 0; i < str.length() - 3; i++) {
            if (str.charAt(i) == str.charAt(i + 3) &&
                    str.charAt(i + 1) == str.charAt(i + 2) &&
                    str.charAt(i) != str.charAt(i + 1)) {
                return true;
            }
        }
        return false;
    }

    public static Set<String> patternFound2(String str) {
        Set<String> sets = Sets.newLinkedHashSet();
        for (int i = 0; i < str.length() - 2; i++) {
            if (str.charAt(i) == str.charAt(i + 2) &&
                    str.charAt(i) != str.charAt(i + 1)) {
                sets.add(str.substring(i, i + 3));
            }
        }
        return sets;
    }

    public static void problem1(List<String> input) {

        long sum = 0;
        outer:
        for (String line : input) {

            String[] strs = line.split("\\[|\\]");
            List<String> outside = IntStream.range(0, strs.length)
                    .filter(i -> i % 2 == 0).mapToObj(i -> strs[i]).collect(Collectors.toList());
            List<String> inside = IntStream.range(0, strs.length)
                    .filter(i -> i % 2 == 1).mapToObj(i -> strs[i]).collect(Collectors.toList());

            for (String in : inside) {
                if (patternFound(in)) {
                    continue outer;
                }
            }

            for (String out : outside) {
                if (patternFound(out)) {
                    sum++;
                    continue outer;
                }
            }

        }

        System.out.println(sum);
    }

    public static void problem2(List<String> input) {

        long sum = 0;
        outer:
        for (String line : input) {

            String[] strs = line.split("\\[|\\]");
            List<String> outside = IntStream.range(0, strs.length)
                    .filter(i -> i % 2 == 0).mapToObj(i -> strs[i]).collect(Collectors.toList());
            List<String> inside = IntStream.range(0, strs.length)
                    .filter(i -> i % 2 == 1).mapToObj(i -> strs[i]).collect(Collectors.toList());

            Set<String> inmasks = Sets.newLinkedHashSet();
            for (String in : inside) {
                inmasks.addAll(patternFound2(in));
            }

            if (inmasks.isEmpty()) continue;

            for (String out : outside) {
                Set<String> outmasks = patternFound2(out);
                for (String outmask : outmasks) {

                    for (String inmask : inmasks) {
                        if (outmask.charAt(0) == inmask.charAt(1) &&
                                outmask.charAt(1) == inmask.charAt(0)) {
                            sum++;
                            continue outer;
                        }

                    }
                }
            }

        }

        System.out.println(sum);
    }

}
