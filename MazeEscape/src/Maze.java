public class Maze {
    int rows;
    int cols;
    String[] map;
    int robotRow;
    int robotCol;
    Coordinate start;
    Coordinate goal;
    int[][] visited;
    int MAX_SIZE = 1000;
    int counter;
    ArrayStack<Coordinate> depthStack;
    ArrayStack<String> path;

    public Maze() {
        // Note: in my real test, I will create much larger
        // and more complicated map
        rows = 7;
        cols = 5;
        map = new String[rows];
        map[0] = ".....";
        map[1] = ". . X";
        map[2] = "... .";
        map[3] = ". . .";
        map[4] = ". . .";
        map[5] = ".   .";
        map[6] = ".....";

        robotRow = 3;
        robotCol = 1;
        start = new Coordinate(robotRow, robotCol, 0);
        goal = new Coordinate();
        counter = 0;
        depthStack = new ArrayStack<Coordinate>();
        path = new ArrayStack<String>();
        visited = new int[MAX_SIZE][MAX_SIZE];
        visited[start.x][start.y] = 1;
    }

    public String go(String direction) {
        int currentRow = robotRow;
        int currentCol = robotCol;
        if (direction.equals("UP")) {
            currentRow--;
        } else if (direction.equals("DOWN")) {
            currentRow++;
        } else if (direction.equals("LEFT")) {
            currentCol--;
        } else {
            currentCol++;
        }

        // check the next position
        if (map[currentRow].charAt(currentCol) == 'X') {
            // Exit gate => update robot location
            robotRow = currentRow;
            robotCol = currentCol;
            goal = new Coordinate(currentRow, currentCol, 0);
            visited[currentRow][currentCol] = 1;
            depthStack.push(new Coordinate(robotRow, robotCol, 0));
            return "win";
        } else if (map[currentRow].charAt(currentCol) == '.') {
            // Wall
            visited[currentRow][currentCol] = -1;
            return "false";
        } else if (visited[currentRow][currentCol] == 1) {
            // Already visited
            return "false";
        }
        else if (visited[currentRow][currentCol] == 2) {
            // Go back => update robot location
            robotRow = currentRow;
            robotCol = currentCol;
            visited[currentRow][currentCol] = 1;
            return "false";
        } else {
            // Space => update robot location
            robotRow = currentRow;
            robotCol = currentCol;
            visited[currentRow][currentCol] = 1;
            depthStack.push(new Coordinate(robotRow, robotCol, 0));
            return "true";
        }
    }

    public void trace() {
        if (depthStack.size() == 1) {
            return;
        }

        int currentX, currentY, previousX, previousY;

        //Coordinate of current tile
        currentX = depthStack.peek().x;
        currentY = depthStack.peek().y;
        depthStack.pop();

        //Coordinate of previous tile
        previousX = depthStack.peek().x;
        previousY = depthStack.peek().y;

        //Check position of 2 tiles to get direction
        if (currentX == previousX) {
            if (currentY == previousY+1) {
                path.push("RIGHT");
            } else {
                path.push("LEFT");
            }
        } else {
            if (currentX == previousX+1) {
                path.push("DOWN");
            } else {
                path.push("UP");
            }
        }

        trace();
    }
    public void display() {
        System.out.println("Number of counters: " + counter);
        while(!path.isEmpty()) {
            System.out.println(path.peek());
            path.pop();
        }
    }

    public static void main(String[] args) {
        (new Robot()).navigate();
    }
}

class Coordinate {
    int x, y, dir;
    Coordinate() {}
    Coordinate(int x, int y, int dir) {
        this.x = x;
        this.y = y;
        this.dir = dir;
    }
}

interface Func {
    void back();
}

class Robot {
    public void navigate() {
        Maze maze = new Maze();
        maze.depthStack.push(new Coordinate(maze.robotRow, maze.robotCol, 0));
        String[] direction = {"UP", "RIGHT", "DOWN", "LEFT"};
        String result = "";

        //DFS
        while (!result.equals("win")) {
            maze.counter++;
            if (maze.depthStack.peek().dir < 4) {
                // Go to 4 adjacent tiles
                result = maze.go(direction[maze.depthStack.peek().dir++]);
            } else {
                // Dead zone => Mark => Go back to previous tile
                maze.visited[maze.robotRow][maze.robotCol] = -1;
                Func goBack = () ->
                {
                    int currentX, currentY, previousX, previousY;

                    //Coordinate of current tile
                    currentX = maze.depthStack.peek().x;
                    currentY = maze.depthStack.peek().y;
                    maze.depthStack.pop();

                    //Coordinate of previous tile
                    previousX = maze.depthStack.peek().x;
                    previousY = maze.depthStack.peek().y;

                    //Mark that the robot revisit a tile to goBack not because of loop
                    maze.visited[previousX][previousY] = 2;

                    //Check position of 2 tiles to get reverse direction
                    if (currentX == previousX) {
                        if (currentY == previousY+1) {
                            maze.go("LEFT");
                        } else {
                            maze.go("RIGHT");
                        }
                    } else {
                        if (currentX == previousX+1) {
                            maze.go("UP");
                        } else {
                            maze.go("DOWN");
                        }
                    }
                };
                goBack.back();
            }
        }

        //Trace the path
        maze.trace();

        //Display counter and path
        maze.display();
    }
}