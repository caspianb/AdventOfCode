package aoc2020;

import util.ResourceReader;

import java.util.Arrays;
import java.util.List;

public class Day4 {
    public static void main(String[] args) {
        var input = ResourceReader.readFile("aoc2020/day4.txt")
                .replaceAll("\r", "")
                .split("\n\n");

        int c1 = 0, c2 = 0;
        for (String passportData : input) {
            passportData = passportData.replaceAll("\n", " ");
            boolean hasRequired = hasRequired(passportData);
            c1 += hasRequired ? 1 : 0;
            c2 += hasRequired && isValid(passportData) ? 1 : 0;
        }

        System.out.println(c1);
        System.out.println(c2);
    }

    static boolean hasRequired(String passport) {
        var required = Arrays.asList(
                "byr:", "iyr:", "eyr:", "hgt:", "hcl:", "ecl:", "pid:");
        return required.stream().allMatch(passport::contains);
    }

    static boolean isValid(String passport) {
        List<String> validPatterns = Arrays.asList(
                "byr:(19[2-9][0-9]|200[0-2])",
                "iyr:(201[0-9]|2020)",
                "eyr:(202[0-9]|2030)",
                "hgt:(1[5-8][0-9]|19[0-3])cm",
                "hgt:(59|6[0-9]|7[0-6])in",
                "hcl:#[0-9a-f]{6}",
                "ecl:(amb|blu|brn|gry|grn|hzl|oth)",
                "pid:[0-9]{9}",
                "cid:.*"
        );

        String[] fields = passport.trim().split(" ");
        return Arrays.stream(fields)
                .allMatch(field -> validPatterns.stream().anyMatch(field::matches));
    }
}
