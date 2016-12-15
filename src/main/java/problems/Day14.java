package problems;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class Day14 {

    private static String input = "cuanljph";
    private static int HASH_TIMES = 2016;

    public static void main(String[] args) throws IOException {

        System.out.println("Running Day14....");
        problem1();
        problem2();
    }

    private static Map<String, String> hashes = Maps.newHashMap();

    public static String hash(String input) {
        return DigestUtils.md5Hex(input);
    }

    public static String hash(String input, int count) {
        String md5 = hashes.get(input);

        if (md5 == null) {
            md5 = input;
            for (int i = 0; i < count + 1; i++) {
                String newMd5 = DigestUtils.md5Hex(md5);
                md5 = newMd5;
            }
            hashes.put(input, md5);
        }

        return md5;
    }

    public static boolean front(String input, int count) {
        char ch = input.charAt(0);
        for (int i = 1; i < count; i++) {
            if (input.charAt(i) != ch) return false;
        }
        return true;
    }

    public static Character match(String input, int count, Character c) {
        for (int i = 0; i < input.length() - count; i++) {
            char ch = (c != null) ? c : input.charAt(i);

            boolean match = true;
            for (int j = 0; j < count; j++) {
                if (input.charAt(i + j) != ch) {
                    match = false;
                    break;
                }
            }

            if (match) {
                return ch;
            }
        }

        return null;
    }

    public static void problem1() {

        Set<Long> valid = Sets.newLinkedHashSet();
        for (long i = 0; i < Long.MAX_VALUE; i++) {
            String md5 = hash(input + i);
            Character ch = match(md5, 3, null);
            if (ch != null) {

                for (long j = i + 1; j <= i + 1000; j++) {

                    String next = hash(input + j);
                    if (match(next, 5, ch) != null) {
                        valid.add(i);
                        break;
                    }
                }
            }

            if (valid.size() == 64) break;
        }

        System.out.println(CollectionUtils.get(valid, 63));

    }

    public static void problem2() {

        Set<Long> valid = Sets.newLinkedHashSet();
        for (long i = 0; i < Long.MAX_VALUE; i++) {
            String md5 = hash(input + i, HASH_TIMES);
            Character ch = match(md5, 3, null);
            if (ch != null) {

                for (long j = i + 1; j <= i + 1000; j++) {

                    String next = hash(input + j, HASH_TIMES);
                    if (match(next, 5, ch) != null) {
                        valid.add(i);
                        break;
                    }
                }
            }

            if (valid.size() == 64) break;
        }

        System.out.println(CollectionUtils.get(valid, 63));

    }

}
