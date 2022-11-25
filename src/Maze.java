import java.util.Random;

class Coordinate {
    int x, y;
    Coordinate() {}
    Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }
    Coordinate(Coordinate pos) {
        this.x = pos.x;
        this.y = pos.y;
    }

    public static boolean isEqual(Coordinate c1, Coordinate c2) {
        return (c1.x == c2.x && c1.y == c2.y);
    }
}

public class Maze {
    int rows;
    int cols;
    String[] map;
    int robotRow;
    int robotCol;
    int steps;

    ArrayStack<Coordinate> deadZone;
    ArrayStack<Coordinate> visited;
    ArrayStack<String> path;

    public Maze() {
        // Note: in my real test, I will create much larger
        // and more complicated map
        rows = 4;
        cols = 5;
        map = new String[rows];
        map[0] = ".....";
        map[1] = ".   X";
        map[2] = ".   .";
        map[3] = ".....";
        Random rd = new Random();
        do {
            robotRow = rd.nextInt(rows - 1);
            System.out.println("row: " + robotRow);
            robotCol = rd.nextInt(cols - 1);
            System.out.println("col: " + robotCol);
        }while (map[robotRow].charAt(robotCol) == '.');

//        robotRow = 0;
//        robotCol = 1;
        steps = 0;
    }

    public boolean isHitDeadZone(Coordinate pos) {
        for (Coordinate coordinate: deadZone.getItems()) {
            if (Coordinate.isEqual(coordinate, pos)) return true;
        }
        return false;
    }

    public boolean isHitOldPath(Coordinate pos) {
        for (Coordinate corCoordinate: visited.getItems()
             ) {
            if (Coordinate.isEqual(corCoordinate, pos)) return true;
        }
        return false;
    }

    public String backDirection(String preDirection) {
        switch (preDirection) {
            case "Up":
                return "DOWN";
            case "DOWN":
                return "Up";
            case "LEFT":
                return "RIGHT";
            case "RIGHT":
                return "LEFT";
        }
        return "";
    }

    public String go(String direction) {
        if (!direction.equals("UP") &&
                !direction.equals("DOWN") &&
                !direction.equals("LEFT") &&
                !direction.equals("RIGHT")) {
            // invalid direction
            steps++;
            return "false";
        }
        int currentRow = robotRow;
        int currentCol = robotCol;
        try {
        if (direction.equals("UP")) {
                currentRow--;
            } else if (direction.equals("DOWN")) {
                currentRow++;
            } else if (direction.equals("LEFT")) {
                currentCol--;
            } else {
                currentCol++;
            }

            Coordinate currentCoordinate = new Coordinate(currentRow, currentCol);

            // check the next position
            if (map[currentRow].charAt(currentCol) == 'X') {
                // Exit gate
                steps++;
                System.out.println("Steps to reach the Exit gate " + steps);
                return "win";
            } else if (map[currentRow].charAt(currentCol) == '.') {
                // Wall
                steps++;
                return "false";
            } else if(isHitDeadZone(currentCoordinate)) {
                // Dead zone
                steps++;
                return "false";
            } else if (isHitOldPath(currentCoordinate)) {
                // Get into a loop
                steps++;
                return "false";
            } else {
                // Space => update robot location
                steps++;
                robotRow = currentRow;
                robotCol = currentCol;
                path.push(direction);
                visited.push(new Coordinate(robotRow, robotCol));
                return "true";
            }
        }
        catch (Exception e) {
            steps++;
            return "false";
        }
    }

    public static void main(String[] args) {
        (new Robot()).navigate();
    }
}

class Robot {
    // A very simple implementation
    // where the robot just go randomly
    public void navigate() {
        Maze maze = new Maze();
        String[] directions = {"LEFT", "DOWN", "RIGHT", "UP"};
        String result = "";
        while (!result.equals("win")) {
            for (String dir: directions
                 ) {
                result = maze.go(dir);
                if (result.equals("true")) break;
            }
            if (result.equals("false")) {
                maze.deadZone.push(new Coordinate(maze.robotRow, maze.robotCol));
                // To fix create a goBack func instead of call the go
                maze.go(maze.backDirection(maze.path.peek()));
                maze.path.pop();
            }
        }
    }
}