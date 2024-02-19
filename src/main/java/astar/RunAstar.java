package astar;

import grid.GridCell;
import grid.GridEnvironment;

import java.util.List;

import static common.Helpers.printGrid;

public class RunAstar {
    public static void main(String[] args) {
        // Create the grid and initialize it with obstacles and charging stations.
        GridEnvironment gridEnvironment = new GridEnvironment(30, 30);

        // Add obstacles and charging stations in random places
//        gridEnvironment.addMultiObstacles(6);
//        gridEnvironment.addMultiChargingStation(5);

        // Add static obstacles and charging stations
        gridEnvironment.addObstacle(20, 20);
        gridEnvironment.addChargingStation(25, 25);

        // Print the grid to visualize the environment
        gridEnvironment.print();

        // Create and initialize the robot position
        Robot robot = new Robot(4, 4, 100); // Initialize the position and battery level.

        // Find the destination using A*
        GridCell destination = new GridCell(1, 21, false, false, false);
        GridCell finalDestination = robot.findDestinationStation(gridEnvironment.gridCells, destination);


        if (finalDestination != null) {
            System.out.println("Final Destination = " + "[" + finalDestination.row + "]" + "[" + finalDestination.col + "]");
        }

        executePath(robot, finalDestination);
        printGrid(gridEnvironment.gridCells);

        calculateGridTraversalTime(gridEnvironment.gridCells);

    }

    private static long calculateGridTraversalTime(GridCell[][] grid) {
        long startTime = System.nanoTime();

        // Traverse all cells
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j].isObstacle) {
                    System.out.println("Obstacle: [" + i + "][" + j + "]");
                } else if (grid[i][j].isChargingStation) {
                    System.out.println("Charging Station: [" + i + "][" + j + "]");
                }

            }
        }
        // Stop measuring time
        long stopTime = System.nanoTime();
        long elapsedTime = stopTime - startTime;
        double elapsedTimeMillis = (double) elapsedTime / 1_000_000; // Convert nanoseconds to milliseconds
        System.out.println("Total time taken to traverse the grid: " + elapsedTimeMillis + " milliseconds");
        return elapsedTime;
    }

    public static void executePath(Robot robot, GridCell destinationStation) {
        List<GridCell> shortestPath = robot.returnShortestPath(destinationStation);

        if (destinationStation != null) {
            if (shortestPath.isEmpty()) {
                System.out.println("No path to the nearest charging station found.");
            } else {
                // Execute the path to reach the charging station.
                int steps = 0;
                for (GridCell cell : shortestPath) {
                    robot.currentRow = cell.row;
                    robot.currentCol = cell.col;
                    robot.batteryLevel--; // Consume battery as the robot moves.
                    steps++;
                    System.out.println("Step " + steps + ": Row " + cell.row + ", Col " + cell.col);
//                    System.out.println("cell = " + cell.distance);
                }
                System.out.println("Reached the nearest charging station.");
            }
        } else {
            System.out.println("No charging station found.");
        }
    }


}
