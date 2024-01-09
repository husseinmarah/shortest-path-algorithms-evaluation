package common;

import bellman.Robot;
import grid.GridCell;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Helpers {

    public static void printPath(GridCell chargingStation) {
        List<GridCell> path = new ArrayList<>();
        while (chargingStation != null) {
            path.add(chargingStation);
            chargingStation = chargingStation.parent;
        }
        Collections.reverse(path);
        int steps = 0;
        for (GridCell cell : path) {
            steps++;
            System.out.println("Step " + steps + ": Row " + cell.row + ", Col " + cell.col);
        }
    }

    public static void printGrid(GridCell[][] gridCells) {
        int cellWidth = 3; // Adjust the cell width as needed

        // Print the grid with equal cell sizes
        for (int i = 0; i < gridCells.length; i++) {
            for (int j = 0; j < gridCells[0].length; j++) {
                String cellContent = null;
                if (gridCells[i][j].isObstacle) {
                    cellContent = "X"; // X represents an obstacle
                } else if (gridCells[i][j].isChargingStation) {
                    cellContent = "C"; // C represents a charging station
                } else if (gridCells[i][j].distance == 0) {
                    cellContent = String.valueOf(0); // 0 represents the current robot's position
                } else if (gridCells[i][j].distance != Integer.MAX_VALUE) {
                    cellContent = String.valueOf(gridCells[i][j].distance); // represents visited cell
                } else {
                    cellContent = "."; // . represents an empty cell
                }
                System.out.printf("%-" + cellWidth + "s", cellContent);
            }
            System.out.println(); // Move to the next row
        }
    }

    public static void printGridDifferently(GridCell[][] grid){
        for (int row = 0; row < grid[0].length; row++) {
            for (int col = 0; col < grid[1].length; col++) {
                if (grid[row][col].isObstacle)
                    System.out.print("X"+"\t"); // X represents an obstacle
                else if (grid[row][col].isChargingStation)
                    System.out.print("C"+"\t"); // C represents a charging station
                else if (grid[row][col].distance==Integer.MAX_VALUE)
                    System.out.print("."+"\t"); // . represents an empty cell
                else if (grid[row][col].distance!=Integer.MAX_VALUE)
                    System.out.print(grid[row][col].distance+"\t");
                else if (grid[row][col].distance==0)
                    System.out.print("0"+"\t"); // 0 represents the robot's current cell
            }
            System.out.println("\n");
        }
    }

}
