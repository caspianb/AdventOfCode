package aoc2016;

import util.ResourceReader;

import java.util.Set;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Sets;

public class Day1 {

    public enum Direction {
        NORTH(0, 1),
        EAST(1, 0),
        SOUTH(0, -1),
        WEST(-1, 0);

        private final int xVector;
        private final int yVector;

        private Direction(int xVector, int yVector) {
            this.xVector = xVector;
            this.yVector = yVector;
        }

        public Direction turnLeft() {
            int newOrd = ordinal() - 1;
            return (values()[newOrd >= 0 ? newOrd : values().length - 1]);
        }

        public Direction turnRight() {
            int newOrd = ordinal() + 1;
            return (values()[newOrd < values().length ? newOrd : 0]);
        }

        public int getXVector() {
            return xVector;
        }

        public int getYVector() {
            return yVector;
        }
    }

    public static void main(String[] args) {
        String[] instructions = ResourceReader.readCsv("aoc2016/input1.txt").toArray(String[]::new);
        Direction currentDirection = Direction.NORTH;
        Set<Object> alreadyVisited = Sets.newHashSet();
        int xPosition = 0;
        int yPosition = 0;

        for (String instruction : instructions) {
            char turnDirection = instruction.charAt(0);
            int instructionDistance = Integer.valueOf(instruction.substring(1));
            currentDirection = (turnDirection == 'L') ? currentDirection.turnLeft() : currentDirection.turnRight();

            for (int i = 0; i < instructionDistance; i++) {
                xPosition += 1 * currentDirection.getXVector();
                yPosition += 1 * currentDirection.getYVector();

                Pair<Integer, Integer> currentPosition = ImmutablePair.of(xPosition, yPosition);
                if (!alreadyVisited.add(currentPosition)) {
                    int totalDistance = Math.abs(xPosition) + Math.abs(yPosition);
                    System.out.println(" - Repeat visit to: " + currentPosition + " at a distance of " + totalDistance);
                }
            }
        }

        int totalDistance = Math.abs(xPosition) + Math.abs(yPosition);
        System.out.println("Shortest distance to final destination: " + totalDistance);
    }
}
