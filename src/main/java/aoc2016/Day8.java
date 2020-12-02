package aoc2016;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

public class Day8 {

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("src/main/resources/aoc2016/input8.txt");
        List<String> input = Files.readAllLines(path).stream()
                .map(StringUtils::stripToEmpty)
                .collect(Collectors.toList());

        problem1(input);

        path = Paths.get("src/main/resources/aoc2016/input8-reddit.txt");
        input = Files.readAllLines(path).stream()
                .map(StringUtils::stripToEmpty)
                .collect(Collectors.toList());

        problemReddit(input);
    }

    private static void print(int[][] screen) {
        for (int i = 0; i < screen.length; i++) {
            for (int j = 0; j < screen[i].length; j++) {
                if (j % 5 == 0) System.out.print(" ");
                char ch = (screen[i][j] == 1) ? 'o' : ' ';
                System.out.print(ch);
            }
            System.out.println();
        }
        System.out.println();
        System.out.println();
    }

    public static void problem1(List<String> input) {
        int[][] screen = new int[6][50];
        for (String line : input) {
            if (line.startsWith("rect ")) {
                String[] pos = line.substring(5).split("x");
                int A = Integer.valueOf(pos[0]);
                int B = Integer.valueOf(pos[1]);

                for (int x = 0; x < A; x++) {
                    for (int y = 0; y < B; y++) {
                        screen[y][x] = 1;
                    }
                }
            }
            else if (line.startsWith("rotate row")) {
                String[] str = line.split(" ");
                int A = Integer.valueOf(str[2].split("=")[1]);
                int B = Integer.valueOf(str[4]);

                int[] row = new int[50];
                for (int i = 0; i < 50; i++) {
                    row[(i + B) % 50] = screen[A][i];
                }
                screen[A] = row;
            }
            else if (line.startsWith("rotate column")) {
                String[] str = line.split(" ");
                int A = Integer.valueOf(str[2].split("=")[1]);
                int B = Integer.valueOf(str[4]);

                int[] col = new int[6];
                for (int i = 0; i < 6; i++) {
                    col[(i + B) % 6] = screen[i][A];
                }

                for (int i = 0; i < 6; i++) {
                    screen[i][A] = col[i];
                }
            }

        }

        print(screen);

        long sum = 0;
        for (int i = 0; i < screen.length; i++) {
            for (int j = 0; j < screen[i].length; j++) {
                sum += screen[i][j];
            }
        }
        System.out.println(sum);
    }

    public static void problemReddit(List<String> input) {
        int[][] screen = new int[6][50];
        for (String line : input) {
            if (line.startsWith("rect ")) {
                String[] pos = line.substring(5).split("x");
                // Wires got crossed! -- https://www.reddit.com/r/adventofcode/comments/5h9sfd/2016_day_8_tampering_detected/
                int B = Integer.valueOf(pos[0]);
                int A = Integer.valueOf(pos[1]);

                for (int x = 0; x < A; x++) {
                    for (int y = 0; y < B; y++) {
                        screen[y][x] = 1;
                    }
                }
            }
            else if (line.startsWith("rotate row")) {
                String[] str = line.split(" ");
                int A = Integer.valueOf(str[2].split("=")[1]);
                int B = Integer.valueOf(str[4]);

                int[] row = new int[50];
                for (int i = 0; i < 50; i++) {
                    row[(i + B) % 50] = screen[A][i];
                }
                screen[A] = row;
            }
            else if (line.startsWith("rotate column")) {
                String[] str = line.split(" ");
                int A = Integer.valueOf(str[2].split("=")[1]);
                int B = Integer.valueOf(str[4]);

                int[] col = new int[6];
                for (int i = 0; i < 6; i++) {
                    col[(i + B) % 6] = screen[i][A];
                }

                for (int i = 0; i < 6; i++) {
                    screen[i][A] = col[i];
                }
            }

        }

        print(screen);
    }

}
