package aoc2016;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;

import com.google.common.collect.Sets;

import util.bfs.BFS;
import util.bfs.State;

public class Day13b {

    protected static final int input = 1362;

    public static void main(String[] args) throws IOException {
        problem1();
        problem2();
    }

    private static class Position extends State {
        private int xPos;
        private int yPos;

        public Position(int xPos, int yPos) {
            super(null);
            this.xPos = xPos;
            this.yPos = yPos;
        }

        protected Position(Position parent, int xPos, int yPos) {
            super(parent);
            this.xPos = xPos;
            this.yPos = yPos;
        }

        public Set<Position> moves() {
            Set<Position> moves = Sets.newLinkedHashSet();

            moves.add(new Position(this, xPos - 1, yPos));
            moves.add(new Position(this, xPos, yPos - 1));
            moves.add(new Position(this, xPos + 1, yPos));
            moves.add(new Position(this, xPos, yPos + 1));

            moves.removeIf(p -> !p.valid());
            return moves;
        }

        public boolean valid() {
            return xPos >= 0 && yPos >= 0 && isWall(xPos, yPos);
        }

        private boolean isWall(int x, int y) {
            long value = (x * x + 3 * x + 2 * x * y + y + y * y) + input;
            return Long.bitCount(value) % 2 == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(xPos, yPos);
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Position)) {
                return false;
            }
            Position other = (Position) obj;
            return xPos == other.xPos &&
                    yPos == other.yPos;
        }

    }

    public static void problem1() {
        State answer = BFS.solve(new Position(1, 1),
                p -> p.xPos == 31 && p.yPos == 39,
                Position::moves);
        System.out.println(answer.steps());
    }

    public static void problem2() {
        Set<Position> answers = BFS.accumulate(new Position(1, 1),
                p -> p.steps() <= 50,
                Position::moves);

        System.out.println(answers.size());
    }
}
