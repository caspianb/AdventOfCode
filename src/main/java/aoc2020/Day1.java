package aoc2020;

import util.ResourceReader;

import java.util.List;

public class Day1 {

    public static void main(String[] args) {
        List<Long> input = ResourceReader.readLineNumbers("aoc2020/day1.txt");
        long answer1 = answer1(input, 2020);
        System.out.println(answer1);

        long answer2 = answer2(input, 2020);
        System.out.println(answer2);
    }

    protected static long answer1(List<Long> input, long targetValue) {
        for (int i = 0; i < input.size() - 1; i++) {
            for (int j = i + 1; j < input.size(); j++) {
                long sum = input.get(i) + input.get(j);
                if (sum == targetValue) {
                    return input.get(i) * input.get(j);
                }
            }
        }
        return -1;
    }

    protected static long answer2(List<Long> input, long targetValue) {
        for (int i = 0; i < input.size() - 2; i++) {
            for (int j = i + 1; j < input.size() - 1; j++) {
                long sum = input.get(i) + input.get(j);
                if (sum > 2020) continue;
                for (int k = j + 1; k < input.size(); k++) {
                    sum = input.get(i) + input.get(j) + input.get(k);
                    if (sum == targetValue) {
                        return input.get(i) * input.get(j) * input.get(k);
                    }
                }
            }
        }
        return -1;
    }
}
