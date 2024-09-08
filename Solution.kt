
import kotlin.math.pow

class Solution {

    private companion object {
        const val TURN_RIGHT = -1
        const val TURN_LEFT = -2
        const val SUPPLEMENT_TO_PRIME_NUMBER = 29 

        val NORTH = intArrayOf(0, 1)
        val EAST = intArrayOf(1, 0)
        val SOUTH = intArrayOf(0, -1)
        val WEST = intArrayOf(-1, 0)
        val RANGE_OF_COORDINATES = intArrayOf(-3 * (10.0).pow(4).toInt(), 3 * (10.0).pow(4).toInt())

        val DIRECTIONS = arrayOf(NORTH, EAST, SOUTH, WEST)
    }

    fun robotSim(commands: IntArray, obstacles: Array<IntArray>): Int {
        val quickAccessToHashObstacles: HashSet<Int> = createQuickAccessToHashObstacles(obstacles)

        var indexDirection = 0
        val currentCoordinates = intArrayOf(0, 0)
        var maxSquaredEuclideanDistance = 0

        for (command in commands) {
            if (command == TURN_LEFT || command == TURN_RIGHT) {
                indexDirection = calculateNewIndexDirection(command, indexDirection)
                continue
            }

            for (step in 1..command) {
                val nextX = currentCoordinates[0] + DIRECTIONS[indexDirection][0]
                val nextY = currentCoordinates[1] + DIRECTIONS[indexDirection][1]

                if (quickAccessToHashObstacles.contains(calculateHashCoordinates(nextX, nextY))) {
                    break
                }

                currentCoordinates[0] = nextX
                currentCoordinates[1] = nextY
                val currentDistanceFromStart = calculateDistanceFromStart(currentCoordinates)

                if (maxSquaredEuclideanDistance < currentDistanceFromStart) {
                    maxSquaredEuclideanDistance = currentDistanceFromStart
                }
            }
        }

        return maxSquaredEuclideanDistance
    }

    private fun calculateNewIndexDirection(turn: Int, indexDirection: Int): Int {
        if (turn == TURN_RIGHT) {
            return (indexDirection + 1) % DIRECTIONS.size
        } else if (turn == TURN_LEFT) {
            return (indexDirection + DIRECTIONS.size - 1) % DIRECTIONS.size
        }
        return indexDirection
    }

    private fun calculateDistanceFromStart(currentCoordinates: IntArray): Int {
        return currentCoordinates[0] * currentCoordinates[0] + currentCoordinates[1] * currentCoordinates[1]
    }

    private fun createQuickAccessToHashObstacles(obstacles: Array<IntArray>): HashSet<Int> {
        val quickAccessToHashObstacles = HashSet<Int>()
        for (coordinates in obstacles) {
            quickAccessToHashObstacles.add(calculateHashCoordinates(coordinates[0], coordinates[1]))
        }
        return quickAccessToHashObstacles
    }

    private fun calculateHashCoordinates(x: Int, y: Int): Int {
        return x + y * (2 * RANGE_OF_COORDINATES[1] + SUPPLEMENT_TO_PRIME_NUMBER)
    }
}
