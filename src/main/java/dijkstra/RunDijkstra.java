package dijkstra;

import grid.GridCell;
import grid.GridEnvironment;
import java.util.List;

import static common.Helpers.printGrid;

public class RunDijkstra {
    public static void main(String[] args) {
        // Create the grid and initialize it with obstacles and charging stations.
        GridEnvironment gridEnvironment = new GridEnvironment(30, 30);

        // Add obstacles and charging stations in random places
//        gridEnvironment.addMultiObstacles(6);
//        gridEnvironment.addMultiChargingStation(5);

        gridEnvironment.addObstacle(20,20);
        gridEnvironment.addChargingStation(25,25);
        gridEnvironment.addChargingStation(21,23);

        // Print the grid to visualize the environment
        gridEnvironment.print();

        // Create and initialize the robot position
        Robot robot = new Robot(4, 4, 100); // Example initial position and battery level.
        // Find the nearest charging station using Dijkstra's algorithm.

        GridCell nearestChargingStation = robot.findNearestChargingStation(gridEnvironment.gridCells);
        if (nearestChargingStation!=null) {
            System.out.println("Nearest Charging Station: " + "[" + nearestChargingStation.row + "]" + "[" + nearestChargingStation.col + "]");
        }

        executePath(robot, nearestChargingStation);
        printGrid(gridEnvironment.gridCells);
    }

    public static void executePath(Robot robot, GridCell nearestChargingStation) {
        List<GridCell> shortestPath = robot.returnShortestPath(nearestChargingStation);

        if (nearestChargingStation != null) {
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