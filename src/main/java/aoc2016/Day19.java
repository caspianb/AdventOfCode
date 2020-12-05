package aoc2016;

import java.io.IOException;
import java.util.List;

import com.google.common.collect.Lists;

// See Josephus Problem
public class Day19 {

    private static final int input = 3005290;

    public static void main(String[] args) throws IOException {
        System.out.println(problem1(input));
        //problem2();

        // Pattern for part 1 (Josephus Problem)
        // Every pow2 -- starts at 1 then increases by 2

        // Part 1 answer ... (input - highest pow2 < input)*2 + 1
        int pow2 = 2097152;
        System.out.println((input - pow2) * 2 + 1);

        // Pattern indicates answer for part2 == input - (highest power of 3 < input)
        int pow3 = 1594323;
        System.out.println(input - pow3);
    }

    public static class Elf {
        int number;
        int presents;

        Elf(int number) {
            this.number = number;
        }

        @Override
        public String toString() {
            //return String.format("[Elf [%s, presents=%s]", number, presents);
            return String.format("[Elf %s]", number);
        }
    }

    public static List<Elf> getElves(int count) {
        List<Elf> elves = Lists.newArrayList();
        for (int i = 0; i < count; i++) {
            Elf elf = new Elf(i + 1);
            elf.presents = 1;
            elves.add(elf);
        }
        return elves;
    }

    public static boolean done(List<Elf> elves) {
        return elves.size() == 1;
    }

    public static List<Elf> swapNext(List<Elf> elves) {
        for (int i = 0; i < elves.size(); i++) {
            Elf curr = elves.get(i);
            Elf next = elves.get((i + 1) % elves.size());

            if (curr.presents == 0) {
                continue;
            }

            curr.presents += next.presents;
            next.presents = 0;
        }

        elves.removeIf(e -> e.presents == 0);
        return elves;
    }

    public static int problem1(int input) {
        List<Elf> elves = getElves(input);
        while (!done(elves)) {
            elves = swapNext(elves);
        }

        //System.out.println(elves.get(0));
        return elves.get(0).number;
    }

    public static List<Elf> swapMid(List<Elf> elves) {
        for (int i = 0; i < elves.size(); i++) {
            int j = (i + elves.size() / 2) % elves.size();
            Elf curr = elves.get(i);
            Elf next = elves.get(j);

            //System.out.println(elves);
            //System.out.format(" - %s steals from %s [%s]%n", i, j, elves.size());

            curr.presents += next.presents;
            next.presents = 0;
            elves.remove(next);

            // If we removed a node *before* our current node, we need to adjust our index to account for the removal...
            i -= (j < i) ? 1 : 0;
        }

        return elves;
    }

    // EXTREMELY SLOW SOLUTION
    public static int problem2VerySlow(int input) {
        List<Elf> elves = getElves(input);

        while (!done(elves)) {
            //System.out.println(" ..." + elves.size());
            elves = swapMid(elves);
        }

        //System.out.println(elves.get(0));
        return elves.get(0).number;
    }

}
