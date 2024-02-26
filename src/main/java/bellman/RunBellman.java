package bellman;

import grid.GridCell;
import grid.GridEnvironment;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static common.Helpers.printGrid;
import static common.Helpers.printPath;
/**
 *@author <a href="https://github.com/husseinmarah">Hussein Marah</a>
 */
/* The implementation of the Bellman-Ford algorithm for finding the shortest path
 * in a grid environment.*/
public class RunBellman {
    public static void main(String[] args) {

        // Create the grid and initialize it with obstacles and charging stations.
        GridEnvironment gridEnvironment = new GridEnvironment(10, 10);

        // Add static obstacles and charging stations
        gridEnvironment.addObstacle(1, 6);
        gridEnvironment.addObstacle(1, 3);
        gridEnvironment.addObstacle(3, 4);
        gridEnvironment.addObstacle(4, 1);
        gridEnvironment.addObstacle(4, 7);
        gridEnvironment.addObstacle(6, 5);
        gridEnvironment.addObstacle(6, 9);
        gridEnvironment.addObstacle(8, 2);
        gridEnvironment.addObstacle(9, 6);
        gridEnvironment.addChargingStation(1, 1);
        gridEnvironment.addChargingStation(2, 7);
        gridEnvironment.addChargingStation(6, 2);
        gridEnvironment.addChargingStation(8, 8);

        gridEnvironment.traverseGrid();

        // Print the grid to visualize the environment
        gridEnvironment.print();

        // Create and initialize the robot position
        Robot robot = new Robot(0, 0, 100); // Example initial position and battery level.
        // Find the nearest charging station using Dijkstra's algorithm.

        // Set the source cell
        GridCell source = gridEnvironment.gridCells[robot.currentRow][robot.currentCol];

        // Run Bellman-Ford algorithm
        robot.bellmanFord(gridEnvironment.gridCells, source);

        // Find the charging station with the minimum distance
        GridCell nearestChargingStation = robot.findNearestChargingStation(gridEnvironment.gridCells);
        if (nearestChargingStation != null) {
            System.out.println("Nearest Charging Station = " + "[" + nearestChargingStation.row + "]" + "[" + nearestChargingStation.col + "]");
            printPath(nearestChargingStation);
        } else {
            System.out.println("No charging station found.");
        }

        printGrid(gridEnvironment.gridCells);
    }

}