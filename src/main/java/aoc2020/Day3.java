package aoc2020;

import util.ResourceReader;

import java.util.List;

public class Day3 {
    public static void main(String[] args) {
        List<String> input = ResourceReader.readLines("aoc2020/day3.txt");
        int answer1 = countTrees(input, 3, 1);
        System.out.println(answer1);

        int answer2a = countTrees(input, 1, 1);
        int answer2b = answer1;
        int answer2c = countTrees(input, 5, 1);
        int answer2d = countTrees(input, 7, 1);
        int answer2e = countTrees(input, 1, 2);

        System.out.println(answer2a * answer2b * answer2c * answer2d * answer2e);
    }

    protected static int countTrees(List<String> input, int colDelta, int rowDelta) {
        int count = 0;
        int col = 0;
        for (int row = 0; row < input.size(); row += rowDelta) {
            if (input.get(row).charAt(col) == '#') count++;
            col = (col + colDelta) % input.get(row).length();
        }
        return count;
    }
}
