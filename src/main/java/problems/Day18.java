package problems;

import java.io.IOException;
import java.util.List;

import com.google.common.collect.Lists;

public class Day18 {

    private static final String input = ".^.^..^......^^^^^...^^^...^...^....^^.^...^.^^^^....^...^^.^^^...^^^^.^^.^.^^..^.^^^..^^^^^^.^^^..^";
    private static final char SAFE = '.';
    private static final char TRAP = '^';

    public static void main(String[] args) throws IOException {
        solve(40);
        solve(400000);
    }

    public static boolean isTrap(String line, int pos) {
        boolean left = (pos > 0) ? line.charAt(pos - 1) == SAFE : true;
        boolean center = line.charAt(pos) == SAFE;
        boolean right = (pos < line.length() - 1) ? line.charAt(pos + 1) == SAFE : true;

        return (left && center && !right) ||
                (!left && center && right) ||
                (left && !center && !right) ||
                (!left && !center && right);
    }

    public static void solve(int numRows) {
        List<String> rows = Lists.newArrayList();
        rows.add(input);

        for (int i = 1; i < numRows; i++) {
            String prev = rows.get(i - 1);
            StringBuilder line = new StringBuilder();
            for (int pos = 0; pos < input.length(); pos++) {
                line.append(isTrap(prev, pos) ? TRAP : SAFE);
            }
            rows.add(line.toString());
        }

        int numsafe = 0;
        for (String row : rows) {
            for (char ch : row.toCharArray()) {
                if (ch == SAFE) numsafe++;
            }
        }

        System.out.println(numsafe);
    }

}
