package aoc2016;

import java.util.Objects;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;

import com.google.common.collect.Sets;

import util.bfs.BFS;
import util.bfs.State;

public class Day17 {

    private static final String input = "vkjiggvb";

    public static String hash(String input) {
        return DigestUtils.md5Hex(input);
    }

    public static void main(String[] args) {
        System.out.println("Running Day17...");
        problem1();
        problem2();
    }

    public static class Position extends State {
        private String path = "";
        private int x;
        private int y;

        public Position(String path, int x, int y) {
            super(null);
            this.path = path;
            this.x = x;
            this.y = y;
        }

        private Position(Position parent, String path, int x, int y) {
            super(parent);
            this.path = path;
            this.x = x;
            this.y = y;

        }

        protected boolean goal() {
            return x == 3 && y == 3;
        }

        protected boolean open(char ch) {
            return ch > 'a' && ch <= 'f';
        }

        protected Set<Position> moves() {
            Set<Position> moves = Sets.newLinkedHashSet();

            if (!goal()) {
                String hash = hash(path);

                if (y > 0 && open(hash.charAt(0))) {
                    moves.add(new Position(this, path + "U", x, y - 1));
                }

                if (y < 3 && open(hash.charAt(1))) {
                    moves.add(new Position(this, path + "D", x, y + 1));
                }

                if (x > 0 && open(hash.charAt(2))) {
                    moves.add(new Position(this, path + "L", x - 1, y));
                }

                if (x < 3 && open(hash.charAt(3))) {
                    moves.add(new Position(this, path + "R", x + 1, y));
                }
            }

            return moves;
        }

        @Override
        public int hashCode() {
            return Objects.hash(path, x, y);
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Position)) {
                return false;
            }

            Position other = (Position) obj;
            return Objects.equals(path, other.path) &&
                    Objects.equals(x, other.x) &&
                    Objects.equals(y, other.y);
        }

        @Override
        public String toString() {
            return String.format("Position [path=%s, (%s, %s)]", path, x, y);
        }
    }

    public static void problem1() {
        Position path = new Position(input, 0, 0);
        Position answer = BFS.solve(path, Position::goal, Position::moves);

        System.out.println(answer.path);
    }

    public static void problem2() {
        Position path = new Position(input, 0, 0);
        Set<Position> all = BFS.accumulate(path, Position::goal, Position::moves);

        Position answer = all.stream()
                .max((a1, a2) -> Integer.compare(a1.steps(), a2.steps()))
                .get();

        System.out.println(answer);
        System.out.println(answer.steps());
    }

}
