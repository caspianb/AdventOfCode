package aoc2016;

import util.ResourceReader;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;

import com.google.common.collect.Maps;

public class Day12 {

    public static void main(String[] args) throws IOException {
        List<String> input = ResourceReader.readLines("aoc2016/input12.txt");

        Map<String, Integer> regs = Maps.newHashMap();

        regs.put("a", 0);
        regs.put("b", 0);
        regs.put("c", 0);
        regs.put("d", 0);
        problem(input, regs);

        regs.put("c", 1);
        problem(input, regs);
    }

    public static void problem(List<String> input, Map<String, Integer> regs) {

        int instr = 0;
        while (instr < input.size()) {
            String line = input.get(instr);

            String[] str = line.split(" ");
            String cmd = str[0];

            if (cmd.equals("cpy")) {
                String reg = str[2];

                if (NumberUtils.isCreatable(str[1])) {
                    regs.put(reg, Integer.valueOf(str[1]));
                }
                else {
                    regs.put(reg, regs.get(str[1]));
                }
            }

            else if (cmd.equals("inc")) {
                String reg = str[1];
                regs.put(reg, regs.get(reg) + 1);
            }

            else if (cmd.equals("dec")) {
                String reg = str[1];
                regs.put(reg, regs.get(reg) - 1);
            }

            else if (cmd.equals("jnz")) {
                int y = Integer.valueOf(str[2]);
                if (NumberUtils.isCreatable(str[1])) {
                    int val = Integer.valueOf(str[1]);
                    if (val != 0) {
                        instr += y;
                        continue;
                    }
                }
                else {
                    String reg = str[1];
                    if (regs.get(reg) != 0) {
                        instr += y;
                        continue;
                    }
                }
            }

            instr++;
        }

        System.out.println(regs);

    }

    public static void problem2(List<String> input) {

        for (String line : input) {

        }

    }

}
