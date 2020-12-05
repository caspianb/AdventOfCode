package aoc2020;

import util.ResourceReader;

public class Day5 {
    public static void main(String[] args) {
        var input = ResourceReader.readLines("aoc2020/day5.txt");

        int[] seatIds = input.stream()
                .mapToInt(Day5::calculateSeatId)
                .sorted()
                .toArray();

        int maxSeatId = seatIds[seatIds.length - 1];
        System.out.println(maxSeatId);

        for (int i = 1; i < seatIds.length; i++) {
            if (seatIds[i] - seatIds[i - 1] == 2) System.out.println(seatIds[i] - 1);
        }
    }

    static int calculateSeatId(String line) {
        return Integer.parseInt(line.replaceAll("[BR]", "1").replaceAll("[FL]", "0"), 2);
    }
}
