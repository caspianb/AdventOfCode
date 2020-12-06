package aoc2020;

import util.ResourceReader;

import java.util.Arrays;
import java.util.List;

public class Day6 {
    public static void main(String[] args) {
        var input = ResourceReader.readBatchedLines("aoc2020/day6.txt", true);
        System.out.println(problem1(input));
        System.out.println(problem2(input));
    }

    static long problem1(List<String> input) {
        return input.stream()
                .mapToLong(line -> line.chars().filter(c -> c != ' ').distinct().count())
                .sum();
    }

    static long problem2(List<String> input) {
        return input.stream()
                .map(l -> l.split(" "))
                .mapToLong(answers -> answers[0].chars()
                        .filter(ch -> Arrays.stream(answers).allMatch(a -> a.indexOf(ch) >= 0))
                        .count()
                )
                .sum();
    }
}
