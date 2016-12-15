package problems;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

public class Day15 {

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("src/main/input/input15.txt");
        List<String> input = Files.readAllLines(path).stream()
                .map(StringUtils::stripToEmpty)
                .collect(Collectors.toList());

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
            super();
            this.number = number;
            this.current = current;
            this.total = total;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T readInput(List<String> input) {
        List<Disk> disks = Lists.newArrayList();
        int number = 1;
        for (String line : input) {
            String[] words = line.split(" ");
            Disk disk = new Disk(number++, Integer.valueOf(words[11]), Integer.valueOf(words[3]));
            disks.add(disk);
        }

        return (T) disks;
    }

    public static void problem(List<Disk> disks) {

        for (long time = 0; time < Long.MAX_VALUE; time++) {

            boolean answer = true;
            for (Disk d : disks) {
                if ((d.current + (time + d.number)) % d.total != 0) {
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
