
#include <span>
#include <array>
#include <vector>
#include <unordered_set>
using namespace std;

/*
The code will run faster with ios::sync_with_stdio(0).
However, this should not be used in production code and interactive problems.
In this particular problem, it is ok to apply ios::sync_with_stdio(0).

Many of the top-ranked C++ solutions for time on leetcode apply this trick,
so, for a fairer assessment of the time percentile of my code
you could outcomment the lambda expression below for a faster run.
*/

/*
const static auto speedup = [] {
    ios::sync_with_stdio(0);
    return nullptr;
}();
*/

class Solution {

    static const int TURN_RIGHT = -1;
    static const int TURN_LEFT = -2;
    static const int SUPPLEMENT_TO_PRIME_NUMBER = 29; 

    static constexpr array<int, 2> NORTH{ 0, 1 };
    static constexpr array<int, 2> EAST{ 1, 0 };
    static constexpr array<int, 2> SOUTH{ 0, -1 };
    static constexpr array<int, 2> WEST{ -1, 0 };
    static constexpr array<int, 2> RANGE_OF_COORDINATES{ -3 * 10000, 3 * 10000 };

    static constexpr array< array<int, 2>, 4> DIRECTIONS{ NORTH, EAST, SOUTH, WEST };

public:
    int robotSim(const vector<int>& commands, const vector<vector<int>>& obstacles) const {
        unordered_set<int> quickAccessToHashObstacles = createQuickAccessToHashObstacles(obstacles);

        int indexDirection = 0;
        array<int, 2> currentCoordinates = { 0, 0 };
        int maxSquaredEuclideanDistance = 0;

        for (const auto& command : commands) {
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

private:
    int calculateNewIndexDirection(int turn, int indexDirection) const {
        if (turn == TURN_RIGHT) {
            return (indexDirection + 1) % DIRECTIONS.size();
        }
        else if (turn == TURN_LEFT) {
            return (indexDirection + DIRECTIONS.size() - 1) % DIRECTIONS.size();
        }
        return indexDirection;
    }

    int calculateDistanceFromStart(span <const int> currentCoordinates) const {
        return currentCoordinates[0] * currentCoordinates[0] + currentCoordinates[1] * currentCoordinates[1];
    }

    unordered_set<int> createQuickAccessToHashObstacles(span<const vector<int>> obstacles)const {
        unordered_set<int> quickAccessToHashObstacles;
        for (const auto& coordinates : obstacles) {
            quickAccessToHashObstacles.insert(calculateHashCoordinates(coordinates[0], coordinates[1]));
        }
        return quickAccessToHashObstacles;
    }

    int calculateHashCoordinates(int x, int y) const {
        return x + y * (2 * RANGE_OF_COORDINATES[1] + SUPPLEMENT_TO_PRIME_NUMBER);
    }
};
