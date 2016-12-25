package problems;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

public class Day22 {

    static class Node {
        private int x;
        private int y;
        private int size;
        private int used;

        public int avail() {
            return size - used;
        }

        public boolean empty() {
            return used == 0;
        }

        public boolean fitsOn(Node other) {
            return this.used <= other.avail() - 0;
        }

        public boolean canFit(Node other) {
            return avail() >= other.used;
        }

        @Override
        public String toString() {
            return String.format("[Node: x=%s, y=%s, size=%s, used=%s, avail=%s]", x, y, size, used, avail());
        }
    }

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("src/main/input/input22.txt");
        List<String> input = Files.readAllLines(path).stream()
                .map(StringUtils::stripToEmpty)
                .filter(line -> line.startsWith("/dev/"))
                .collect(Collectors.toList());

        List<Node> nodes = Lists.newArrayList();
        Pattern pattern = Pattern.compile("node-x(\\d+)-y(\\d+)\\h+(\\d+)T\\h+(\\d+)T\\h+(\\d+)");
        for (String line : input) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                Node node = new Node();
                node.x = Integer.valueOf(matcher.group(1));
                node.y = Integer.valueOf(matcher.group(2));
                node.size = Integer.valueOf(matcher.group(3));
                node.used = Integer.valueOf(matcher.group(4));
                nodes.add(node);
            }
        }

        assert (nodes.size() == 1020);

        problem1(nodes);
        problem2();
    }

    public static void problem1(List<Node> nodes) {
        int pairs = 0;
        for (int i = 0; i < nodes.size(); i++) {
            for (int j = 0; j < nodes.size(); j++) {
                if (i != j) {
                    Node a = nodes.get(i);
                    Node b = nodes.get(j);

                    if (!a.empty() && a.fitsOn(b)) {
                        pairs++;
                    }
                }
            }
        }

        System.out.println("viable pairs=" + pairs);

    }

    public static void problem2() {
    }

}
