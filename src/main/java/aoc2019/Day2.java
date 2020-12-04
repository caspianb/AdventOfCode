package aoc2019;

import util.ResourceReader;

public class Day2 {
    public static void main(String[] args) {
        long result = calc(12, 2);
        System.out.println(result);

        for (int noun = 0; noun <= 99; noun++) {
            for (int verb = 0; verb <= 99; verb++) {
                result = calc(noun, verb);
                if (result == 19690720) {
                    System.out.println(String.format("noun=%s, verb=%s, result=%s", noun, verb, result));
                }
            }
        }
    }

    protected static long calc(int noun, int verb) {
        long[] opcodes = ResourceReader.readCsvNumbers("aoc2019/input2.txt").stream()
                .mapToLong(Long::intValue).toArray();

        opcodes[1] = noun;
        opcodes[2] = verb;

        for (int pos = 0; opcodes[pos] != 99; pos += 4) {
            int code = (int) opcodes[pos];
            int pos1 = (int) opcodes[pos + 1];
            int pos2 = (int) opcodes[pos + 2];
            int resPos = (int) opcodes[pos + 3];

            //System.out.println(String.format("%s,%s,%s,%s", code, pos1, pos2, resPos));
            switch (code) {
                case 1:
                    opcodes[resPos] = opcodes[pos1] + opcodes[pos2];
                    break;
                case 2:
                    opcodes[resPos] = opcodes[pos1] * opcodes[pos2];
                    break;
            }
        }

        return opcodes[0];
    }

}
