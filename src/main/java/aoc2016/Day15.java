package aoc2016;

import util.ResourceReader;

import java.io.IOException;
import java.util.List;

import com.google.common.collect.Lists;

public class Day15 {

    public static void main(String[] args) throws IOException {
        List<String> input = ResourceReader.readLines("aoc2016/input15.txt");

        System.out.println("Running Day15...");

        List<Disk> disks = readInput(input);
        problem(disks);

        disks.add(new Disk(disks.size() + 1, 0, 11));
        problem(disks);
    }

    public static class Disk {
        int number;
        int current;
        int total;

        public Disk(int number, int current, int total) {
            this.number = number;
            this.current = current;
            this.total = total;
        }

        public boolean closed(long time) {
            return (current + (time + number)) % total != 0;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T readInput(List<String> input) {
        List<Disk> disks = Lists.newArrayList();
        int number = 1;
        for (String line : input) {
            String[] words = line.split(" ");
            Disk disk = new Disk(number++, Integer.valueOf(words[11].replace(".", "")), Integer.valueOf(words[3]));
            disks.add(disk);
        }

        return (T) disks;
    }

    public static void problem(List<Disk> disks) {

        for (long time = 0; time < Long.MAX_VALUE; time++) {

            boolean answer = true;
            for (Disk d : disks) {
                if (d.closed(time)) {
                    answer = false;
                    break;
                }
            }

            if (answer) {
                System.out.println(time);
                break;
            }
        }
    }

}
