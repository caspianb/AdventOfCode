package problems;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Sets;

public class Day1 {

    private static final String PROBLEM_INPUT = "R3, L5, R2, L1, L2, R5, L2, R2, L2, L2, L1, R2, L2, R4, R4, R1, L2, L3, R3, L1, R2, L2, L4, R4, R5, L3, R3, L3, L3, R4, R5, L3, R3, L5, L1, L2, R2, L1, R3, R1, L1, R187, L1, R2, R47, L5, L1, L2, R4, R3, L3, R3, R4, R1, R3, L1, L4, L1, R2, L1, R4, R5, L1, R77, L5, L4, R3, L2, R4, R5, R5, L2, L2, R2, R5, L2, R194, R5, L2, R4, L5, L4, L2, R5, L3, L2, L5, R5, R2, L3, R3, R1, L4, R2, L1, R5, L1, R5, L1, L1, R3, L1, R5, R2, R5, R5, L4, L5, L5, L5, R3, L2, L5, L4, R3, R1, R1, R4, L2, L4, R5, R5, R4, L2, L2, R5, R5, L5, L2, R4, R4, L4, R1, L3, R1, L1, L1, L1, L4, R5, R4, L4, L4, R5, R3, L2, L2, R3, R1, R4, L3, R1, L4, R3, L3, L2, R2, R2, R2, L1, L4, R3, R2, R2, L3, R2, L3, L2, R4, L2, R3, L4, R5, R4, R1, R5, R3";

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
        String[] instructions = StringUtils.split(PROBLEM_INPUT, ", ");
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
