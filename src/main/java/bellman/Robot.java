package bellman;

import grid.GridCell;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *@author <a href="https://github.com/husseinmarah">Hussein Marah</a>
 */
public class Robot {
private static final int INF = Integer.MAX_VALUE;
static GridCell currentCell;
int batteryLevel;
int currentRow, currentCol;

public Robot(int currentRow, int currentCol, int batteryLevel) {
    this.currentRow = currentRow;
    this.currentCol = currentCol;
    this.batteryLevel = batteryLevel;
    this.currentCell = new GridCell(currentRow, currentCol, false, false, true);

}

public static GridCell getCurrentCell() {
    return currentCell;
}


public static void bellmanFord(GridCell[][] grid, GridCell source) {
    int numRows = grid.length;
    int numCols = grid[0].length;
    source.distance = 0;

    // Relax edges repeatedly
    for (int r = source.row; r < numRows; r++) {
        for (int c = source.col; c < numCols; c++) {
            GridCell current = grid[r][c];
            if (!current.isObstacle) {
                // Explore neighbors (up, down, left, right)
//                    exploreNeighbor(grid, current, r - 1, c);
                exploreNeighbor(grid, current, r + 1, c);
//                    exploreNeighbor(grid, current, r, c - 1);
                exploreNeighbor(grid, current, r, c + 1);
            }
        }
        for (int c = source.col; c >= 0; c--) {
            GridCell current = grid[r][c];
            if (!current.isObstacle) {
                // Explore neighbors (up, down, left, right)
                exploreNeighbor(grid, current, r - 1, c);
//                    exploreNeighbor(grid, current, r + 1, c);
                exploreNeighbor(grid, current, r, c - 1);
//                    exploreNeighbor(grid, current, r, c + 1);
            }
        }
    }
    for (int r = source.row; r >= 0; r--) {
        for (int c = source.col; c < numCols; c++) {
            GridCell current = grid[r][c];
            if (!current.isObstacle) {
                // Explore neighbors (up, down, left, right)
                    exploreNeighbor(grid, current, r - 1, c);
//                exploreNeighbor(grid, current, r + 1, c);
                    exploreNeighbor(grid, current, r, c - 1);
//                exploreNeighbor(grid, current, r, c + 1);
            }
        }
        for (int c = source.col; c >= 0; c--) {
            GridCell current = grid[r][c];
            if (!current.isObstacle) {
                // Explore neighbors (up, down, left, right)
                exploreNeighbor(grid, current, r - 1, c);
//                    exploreNeighbor(grid, current, r + 1, c);
//                exploreNeighbor(grid, current, r, c - 1);
                    exploreNeighbor(grid, current, r, c + 1);
            }
        }
    }


//    // Check for negative cycles (optional)
//        for (int r = 0; r < numRows; r++) {
//        for (int c = 0; c < numCols; c++) {
//            GridCell current = grid[r][c];
//
//            if (!current.isObstacle) {
//                exploreNeighbor(grid, current, r - 1, c);
//                exploreNeighbor(grid, current, r + 1, c);
//                exploreNeighbor(grid, current, r, c - 1);
//                exploreNeighbor(grid, current, r, c + 1);
//            }
//        }
//    }

//    // Print shortest distances
//        System.out.println("Shortest distances from source " + source.row + "," + source.col + ":");
//        for (int r = 0; r < numRows; r++) {
//        for (int c = 0; c < numCols; c++) {
//            GridCell current = grid[r][c];
//            System.out.println("To " + current.row + "," + current.col + ": " + current.distance);
//        }
//    }
}

private static void exploreNeighbor(GridCell[][] grid, GridCell current, int newRow, int newCol) {
    int numRows = grid.length;
    int numCols = grid[0].length;

    if (newRow >= 0 && newRow < numRows && newCol >= 0 && newCol < numCols) {
        GridCell neighbor = grid[newRow][newCol];

        if (!neighbor.isObstacle && current.distance < neighbor.distance) {
            neighbor.distance = current.distance + 1;
            neighbor.parent = current;
        }
    }
}

public GridCell findNearestChargingStation(GridCell[][] grid) {
    int numRows = grid.length;
    int numCols = grid[0].length;
    GridCell chargingStation = null;
    int minDistance = Integer.MAX_VALUE;

    for (int i = 0; i < numRows; i++) {
        for (int j = 0; j < numCols; j++) {
            if (grid[i][j].isChargingStation && grid[i][j].distance < minDistance) {
                chargingStation = grid[i][j];
                minDistance = grid[i][j].distance;
            }
        }
    }

    return chargingStation;
}

public List<GridCell> returnShortestPath(GridCell chargingStation) {
    if (chargingStation != null) {
        return reconstructPath(chargingStation);
    } else {
        return new ArrayList<>(); // No path to any charging station found
    }
}

private List<GridCell> reconstructPath(GridCell chargingStation) {
    List<GridCell> path = new ArrayList<>();
    GridCell currentCell = chargingStation;

    while (currentCell != null) {
        path.add(currentCell);
        currentCell = currentCell.parent;
    }

    Collections.reverse(path);
    return path;
}
}
