
import java.util.HashSet;
import java.util.Set;

public class Solution {

    private static final int TURN_RIGHT = -1;
    private static final int TURN_LEFT = -2;
    private static final int SUPPLEMENT_TO_PRIME_NUMBER = 29;

    private static final int[] NORTH = {0, 1};
    private static final int[] EAST = {1, 0};
    private static final int[] SOUTH = {0, -1};
    private static final int[] WEST = {-1, 0};
    private static final int[] RANGE_OF_COORDINATES = {-3 * (int) Math.pow(10, 4), 3 * (int) Math.pow(10, 4)};

    private static final int[][] DIRECTIONS = {NORTH, EAST, SOUTH, WEST};

    public int robotSim(int[] commands, int[][] obstacles) {
        Set<Integer> quickAccessToHashObstacles = createQuickAccessToHashObstacles(obstacles);

        int indexDirection = 0;
        int[] currentCoordinates = {0, 0};
        int maxSquaredEuclideanDistance = 0;

        for (int command : commands) {
            if (command == TURN_LEFT || command == TURN_RIGHT) {
                indexDirection = calculateNewIndexDirection(command, indexDirection);
                continue;
            }

            for (int step = 1; step <= command; ++step) {
                int nextX = currentCoordinates[0] + DIRECTIONS[indexDirection][0];
                int nextY = currentCoordinates[1] + DIRECTIONS[indexDirection][1];

                if (quickAccessToHashObstacles.contains(calculateHashCoordinates(nextX, nextY))) {
                    break;
                }

                currentCoordinates[0] = nextX;
                currentCoordinates[1] = nextY;
                int currentDistanceFromStart = calculateDistanceFromStart(currentCoordinates);

                if (maxSquaredEuclideanDistance < currentDistanceFromStart) {
                    maxSquaredEuclideanDistance = currentDistanceFromStart;
                }
            }
        }

        return maxSquaredEuclideanDistance;
    }

    private int calculateNewIndexDirection(int turn, int indexDirection) {
        if (turn == TURN_RIGHT) {
            return (indexDirection + 1) % DIRECTIONS.length;
        } else if (turn == TURN_LEFT) {
            return (indexDirection + DIRECTIONS.length - 1) % DIRECTIONS.length;
        }
        return indexDirection;
    }

    private int calculateDistanceFromStart(int[] currentCoordinates) {
        return currentCoordinates[0] * currentCoordinates[0] + currentCoordinates[1] * currentCoordinates[1];
    }

    private Set<Integer> createQuickAccessToHashObstacles(int[][] obstacles) {
        Set<Integer> quickAccessToHashObstacles = new HashSet<>();
        for (int[] coordinates : obstacles) {
            quickAccessToHashObstacles.add(calculateHashCoordinates(coordinates[0], coordinates[1]));
        }
        return quickAccessToHashObstacles;
    }

    private int calculateHashCoordinates(int x, int y) {
        return x + y * (2 * RANGE_OF_COORDINATES[1] + SUPPLEMENT_TO_PRIME_NUMBER);
    }
}
