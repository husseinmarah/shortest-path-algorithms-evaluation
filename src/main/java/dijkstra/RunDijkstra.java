package dijkstra;

import grid.GridCell;
import grid.GridEnvironment;
import java.util.List;

import static common.Helpers.printGrid;
/**
 *@author <a href="https://github.com/husseinmarah">Hussein Marah</a>
 */
public class RunDijkstra {
    public static void main(String[] args) {
        // Create the grid and initialize it with obstacles and charging stations.
        GridEnvironment gridEnvironment = new GridEnvironment(10, 10);

        // Add obstacles and charging stations in random places
//        gridEnvironment.addMultiObstacles(6);
//        gridEnvironment.addMultiChargingStation(5);

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
        // Print the grid to visualize the environment
        gridEnvironment.print();

        // Create and initialize the robot position
        Robot robot = new Robot(0, 0, 100); // Example initial position and battery level.
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
