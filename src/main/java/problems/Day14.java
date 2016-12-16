package problems;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.digest.DigestUtils;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Sets;

public class Day14 {

    private static String input = "cuanljph";
    private static int HASH_TIMES = 2016;

    public static void main(String[] args) throws IOException {
        System.out.println("Running Day14....");
        solve(0);
        solve(HASH_TIMES);
    }

    private static Map<String, String> hashes = new HashMap<>();

    public static String hash(String input, int count) {
        return hashes.computeIfAbsent(input, inp -> {
            for (int i = 0; i < count + 1; i++) {
                inp = DigestUtils.md5Hex(inp);
            }
            return inp;
        });
    }

    public static Character match(String input, int count, Character ch) {
        Pattern pattern = Pattern.compile(String.format("(%s)\\1{%s}",
                MoreObjects.firstNonNull(ch, "."), count - 1));
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return matcher.group(0).charAt(0);
        }

        return null;
    }

    public static void solve(int iterations) {
        SortedSet<Integer> valid = Sets.newTreeSet();
        hashes.clear();

        int index = 0;
        while (valid.size() < 64) {
            String md5 = hash(input + index, iterations);
            Character ch = match(md5, 3, null);
            if (ch != null) {

                for (int i = index + 1; i <= index + 1000; i++) {
                    String next = hash(input + i, iterations);
                    if (match(next, 5, ch) != null) {
                        valid.add(index);
                        break;
                    }
                }
            }

            index++;
        }

        System.out.println(valid.last());
    }

}
