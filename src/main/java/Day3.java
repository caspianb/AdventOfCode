import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

public class Day3 {

    public static void main(String[] args) throws IOException {
        problem1();
        problem2();
    }

    public static void problem1() throws IOException {
        List<String> input = Files.readAllLines(Paths.get("src/main/java/input3.txt"));
        int possible = 0;
        for (String line : input) {
            String[] numbers = line.trim().split("\\h+");

            List<Integer> list = Lists.newArrayList();
            for (String number : numbers) {
                list.add(Integer.valueOf(number));
            }

            Collections.sort(list);
            if (list.get(0) + list.get(1) > list.get(2)) {
                possible++;
            }
        }
        System.out.println(possible);
    }

    public static void problem2() throws IOException {
        List<String> input = Files.readAllLines(Paths.get("src/main/java/input3.txt"));
        int possible = 0;

        List<Integer> col1 = Lists.newArrayList();
        List<Integer> col2 = Lists.newArrayList();
        List<Integer> col3 = Lists.newArrayList();

        for (String line : input) {
            String[] numbers = line.trim().split("\\h+");

            col1.add(Integer.valueOf(numbers[0]));
            col2.add(Integer.valueOf(numbers[1]));
            col3.add(Integer.valueOf(numbers[2]));

            if (col1.size() == 3) {
                Collections.sort(col1);
                Collections.sort(col2);
                Collections.sort(col3);

                if (col1.get(0) + col1.get(1) > col1.get(2)) possible++;
                if (col2.get(0) + col2.get(1) > col2.get(2)) possible++;
                if (col3.get(0) + col3.get(1) > col3.get(2)) possible++;

                col1.clear();
                col2.clear();
                col3.clear();
            }
        }
        System.out.println(possible);
    }
}
