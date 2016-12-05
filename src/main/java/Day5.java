import java.security.NoSuchAlgorithmException;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;

import com.google.common.collect.Sets;

public class Day5 {

    public static void main(String[] args) throws Exception {
        String input = "cxdnnyjw";

        problem1(input);
        problem2(input);
    }

    public static void problem1(String input) throws NoSuchAlgorithmException {
        String password = "";
        for (int i = 0; i <= Long.MAX_VALUE; i++) {
            String cipher = DigestUtils.md5Hex(input + i);

            if (cipher.startsWith("00000")) {
                char value = cipher.charAt(5);
                password += value;
            }

            if (password.length() == 8) {
                break;
            }
        }

        System.out.println(password);
    }

    public static void problem2(String input) {
        char[] password = new char[8];
        Set<Character> positions = Sets.newHashSet();
        for (int i = 0; i <= Long.MAX_VALUE; i++) {
            String cipher = DigestUtils.md5Hex(input + i);

            if (cipher.startsWith("00000")) {
                char pos = cipher.charAt(5);
                char value = cipher.charAt(6);
                if (pos >= '0' && pos < '8' && positions.add(pos)) {
                    int position = Integer.valueOf(pos - '0');
                    password[position] = value;
                }
            }

            if (positions.size() == 8) {
                break;
            }
        }

        System.out.println(new String(password));
    }

}
