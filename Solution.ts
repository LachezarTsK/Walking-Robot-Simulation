
function robotSim(commands: number[], obstacles: number[][]): number {
    this.TURN_RIGHT = -1;
    this.TURN_LEFT = -2;
    this.SUPPLEMENT_TO_PRIME_NUMBER = 29;

    this.NORTH = [0, 1];
    this.EAST = [1, 0];
    this.SOUTH = [0, -1];
    this.WEST = [-1, 0];
    this.RANGE_OF_COORDINATES = [-3 * Math.pow(10, 4), 3 * Math.pow(10, 4)];

    this.DIRECTIONS = [this.NORTH, this.EAST, this.SOUTH, this.WEST];

    const quickAccessToHashObstacles: Set<number> = createQuickAccessToHashObstacles(obstacles);

    let indexDirection = 0;
    let currentCoordinates = [0, 0];
    let maxSquaredEuclideanDistance = 0;

    for (let command of commands) {
        if (command === this.TURN_LEFT || command === this.TURN_RIGHT) {
            indexDirection = calculateNewIndexDirection(command, indexDirection);
            continue;
        }

        for (let step = 1; step <= command; ++step) {
            let nextX = currentCoordinates[0] + this.DIRECTIONS[indexDirection][0];
            let nextY = currentCoordinates[1] + this.DIRECTIONS[indexDirection][1];

            if (quickAccessToHashObstacles.has(calculateHashCoordinates(nextX, nextY))) {
                break;
            }

            currentCoordinates[0] = nextX;
            currentCoordinates[1] = nextY;
            let currentDistanceFromStart = calculateDistanceFromStart(currentCoordinates);

            if (maxSquaredEuclideanDistance < currentDistanceFromStart) {
                maxSquaredEuclideanDistance = currentDistanceFromStart;
            }
        }
    }

    return maxSquaredEuclideanDistance;
};

function calculateNewIndexDirection(turn: number, indexDirection: number): number {
    if (turn === this.TURN_RIGHT) {
        return (indexDirection + 1) % this.DIRECTIONS.length;
    } else if (turn === this.TURN_LEFT) {
        return (indexDirection + this.DIRECTIONS.length - 1) % this.DIRECTIONS.length;
    }
    return indexDirection;
}

function calculateDistanceFromStart(currentCoordinates: number[]): number {
    return currentCoordinates[0] * currentCoordinates[0] + currentCoordinates[1] * currentCoordinates[1];
}

function createQuickAccessToHashObstacles(obstacles: number[][]): Set<number> {
    const quickAccessToHashObstacles: Set<number> = new Set();
    for (let coordinates of obstacles) {
        quickAccessToHashObstacles.add(calculateHashCoordinates(coordinates[0], coordinates[1]));
    }
    return quickAccessToHashObstacles;
}

function calculateHashCoordinates(x: number, y: number): number {
    return x + y * (2 * this.RANGE_OF_COORDINATES[1] + this.SUPPLEMENT_TO_PRIME_NUMBER);
}
