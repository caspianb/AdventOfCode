package aoc2019;

import util.ResourceReader;

public class Day1 {
    public static void main(String[] args) {
        var modules = ResourceReader.readLineNumbers("aoc2019/input1.txt");

        var result1 = modules.stream()
                .mapToLong(mass -> (mass / 3) - 2)
                .sum();
        System.out.println("1: " + result1);

        var result2 = modules.stream()
                .mapToLong(Day1::calcFuel)
                .sum();
        System.out.println("2: " + result2);
    }

    public static long calcFuel(long mass) {
        long fuel = (mass / 3) - 2;
        return fuel <= 0 ? 0 : fuel + calcFuel(fuel);
    }
}
