
package main

import (
    "fmt"
    "math"
)

const TURN_RIGHT = -1
const TURN_LEFT = -2
const SUPPLEMENT_TO_PRIME_NUMBER = 29

var NORTH = [2]int{0, 1}
var EAST = [2]int{1, 0}
var SOUTH = [2]int{0, -1}
var WEST = [2]int{-1, 0}
var RANGE_OF_COORDINATES = [2]int{-3 * int(math.Pow(10.0, 4.0)), 3 * int(math.Pow(10.0, 4.0))}

var DIRECTIONS = [4][2]int{NORTH, EAST, SOUTH, WEST}

type HashSet struct {
    container map[int]bool
}

func robotSim(commands []int, obstacles [][]int) int {
    var quickAccessToHashObstacles HashSet = createQuickAccessToHashObstacles(obstacles)

    indexDirection := 0
    currentCoordinates := []int{0, 0}
    maxSquaredEuclideanDistance := 0

    for _, command := range commands {
        if command == TURN_LEFT || command == TURN_RIGHT {
            indexDirection = calculateNewIndexDirection(command, indexDirection)
            continue
        }

        for step := 1; step <= command; step++ {
            nextX := currentCoordinates[0] + DIRECTIONS[indexDirection][0]
            nextY := currentCoordinates[1] + DIRECTIONS[indexDirection][1]

            if _, contains := quickAccessToHashObstacles.container[calculateHashCoordinates(nextX, nextY)]; contains {
                break
            }

            currentCoordinates[0] = nextX
            currentCoordinates[1] = nextY
            currentDistanceFromStart := calculateDistanceFromStart(currentCoordinates)

            if maxSquaredEuclideanDistance < currentDistanceFromStart {
                maxSquaredEuclideanDistance = currentDistanceFromStart
            }
        }
    }

    return maxSquaredEuclideanDistance
}

func calculateNewIndexDirection(turn int, indexDirection int) int {
    if turn == TURN_RIGHT {
        return (indexDirection + 1) % len(DIRECTIONS)
    } else if turn == TURN_LEFT {
        return (indexDirection + len(DIRECTIONS) - 1) % len(DIRECTIONS)
    }
    return indexDirection
}

func calculateDistanceFromStart(currentCoordinates []int) int {
    return currentCoordinates[0] * currentCoordinates[0] + currentCoordinates[1] * currentCoordinates[1]
}

func createQuickAccessToHashObstacles(obstacles [][]int) HashSet {
    quickAccessToHashObstacles := HashSet{container: map[int]bool{}}
    for _, coordinates := range obstacles {
        quickAccessToHashObstacles.container[calculateHashCoordinates(coordinates[0], coordinates[1])] = true
    }
    return quickAccessToHashObstacles
}

func calculateHashCoordinates(x int, y int) int {
    return x + y * (2 * RANGE_OF_COORDINATES[1] + SUPPLEMENT_TO_PRIME_NUMBER)
}
