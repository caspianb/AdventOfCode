package aoc2020;

import util.ResourceReader;

import java.util.List;

public class Day2 {
    public static void main(String[] args) {
        List<String> input = ResourceReader.readLines("aoc2020/day2.txt");

        int totalValid1 = 0;
        int totalValid2 = 0;
        for (String line : input) {
            String[] arr = line.split(" ");
            int min = Integer.parseInt(arr[0].split("-")[0]);
            int max = Integer.parseInt(arr[0].split("-")[1]);
            char ch = arr[1].charAt(0);
            String password = arr[2];

            totalValid1 += isValid1(min, max, ch, password) ? 1 : 0;
            totalValid2 += isValid2(min - 1, max - 1, ch, password) ? 1 : 0;
        }

        System.out.println(totalValid1);
        System.out.println(totalValid2);
    }

    protected static boolean isValid1(int min, int max, char ch, String password) {
        long count = password.chars()
                .filter(c -> c == ch)
                .count();
        return min <= count && count <= max;
    }

    protected static boolean isValid2(int pos1, int pos2, char ch, String password) {
        return (password.charAt(pos1) == ch) ^ (password.charAt(pos2) == ch);
    }

}
