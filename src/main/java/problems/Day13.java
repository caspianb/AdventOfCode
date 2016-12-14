package problems;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;
import java.util.stream.IntStream;

import com.google.common.collect.Sets;

public class Day13 {

    public static void main(String[] args) throws IOException {
        int input = 1362;

        problem1(input);
        problem2(input);
    }

    private static class Position {
        private final int input;
        private Position parent = null;
        private int xPos;
        private int yPos;

        public Position(int input, int xPos, int yPos) {
            this.input = input;
            this.xPos = xPos;
            this.yPos = yPos;
        }

        protected Position(Position parent, int xPos, int yPos) {
            this.input = parent.input;
            this.parent = parent;
            this.xPos = xPos;
            this.yPos = yPos;
        }

        public Set<Position> getMoves() {
            Set<Position> moves = Sets.newLinkedHashSet();

            moves.add(new Position(this, xPos - 1, yPos));
            moves.add(new Position(this, xPos, yPos - 1));
            moves.add(new Position(this, xPos + 1, yPos));
            moves.add(new Position(this, xPos, yPos + 1));

            moves.removeIf(p -> !p.isValid());
            return moves;
        }

        public boolean isValid() {
            return xPos >= 0 && yPos >= 0 && isWall(input, xPos, yPos);
        }

        public int steps() {
            int steps = 0;
            Position pos = this.parent;
            while (pos != null) {
                steps++;
                pos = pos.parent;
            }
            return steps;
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

    public static char[][] buildMaze(int input, int width, int height) {
        char[][] maze = new char[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                maze[y][x] = isWall(input, x, y) ? '.' : '#';
            }
        }

        // Final destination
        maze[1][1] = 'O';
        maze[31][39] = 'X';

        return maze;
    }

    public static boolean isWall(int input, int x, int y) {
        long value = x * x + 3 * x + 2 * x * y + y + y * y;
        value += input;
        String str = Long.toBinaryString(value);
        long count = IntStream.range(0, str.length()).filter(i -> str.charAt(i) == '1').count();

        return count % 2 == 0;
    }

    public static void printMaze(char[][] maze) {
        for (int x = 0; x < maze.length; x++) {
            for (int y = 0; y < maze.length; y++) {
                System.out.print(maze[y][x]);
            }
            System.out.println();
        }
    }

    public static void problem1(int input) {
        Set<Position> history = Sets.newHashSet();
        Position init = new Position(input, 1, 1);
        Set<Position> positions = Sets.newLinkedHashSet();
        positions.add(init);

        Position answer = null;
        outer:
        while (!positions.isEmpty()) {
            Set<Position> newPositions = Sets.newLinkedHashSet();

            for (Position pos : positions) {
                if (pos.xPos == 31 && pos.yPos == 39) {
                    answer = pos;
                    break outer;
                }
            }

            for (Position pos : positions) {
                history.add(pos);
                newPositions.addAll(pos.getMoves());
            }
            newPositions.removeAll(history);
            positions = newPositions;
        }

        if (answer != null) {
            System.out.println(answer.steps());
        }

    }

    public static void problem2(int input) {
        Set<Position> history = Sets.newHashSet();
        Position init = new Position(input, 1, 1);
        Set<Position> positions = Sets.newLinkedHashSet();
        positions.add(init);

        while (!positions.isEmpty()) {
            Set<Position> newPositions = Sets.newLinkedHashSet();

            for (Position pos : positions) {
                if (pos.steps() <= 50) {
                    history.add(pos);
                    newPositions.addAll(pos.getMoves());
                }
            }

            newPositions.removeAll(history);
            positions = newPositions;
        }

        System.out.println(history.size());
    }
}
