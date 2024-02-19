package dijkstra;

import grid.GridCell;
import java.util.*;
/**
 *@author <a href="https://github.com/husseinmarah">Hussein Marah</a>
 */
class Robot {
    int batteryLevel;
    int currentRow, currentCol;
    static int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // Possible movement directions (up, down, left, right)

    public Robot(int currentRow, int currentCol, int batteryLevel) {
        this.currentRow = currentRow;
        this.currentCol = currentCol;
        this.batteryLevel = batteryLevel;
    }

    public GridCell[][] usingManhattanDistance(GridCell[][] grid, GridCell currentCell, int[][] directions, PriorityQueue<GridCell> pq) {
        // This loop enables the robot to move only in four directions (using Manhattan distance)
        for (int[] dir : directions) {
            int newRow = currentCell.row + dir[0];
            int newCol = currentCell.col + dir[1];

            if (isValid(newRow, newCol, grid) && !grid[newRow][newCol].isObstacle && !grid[newRow][newCol].isCurrent) {
                int newDistance = currentCell.distance + 1;

                if (newDistance < grid[newRow][newCol].distance) {
                    grid[newRow][newCol].distance = newDistance;
                    grid[newRow][newCol].parent = currentCell;
                    pq.add(grid[newRow][newCol]);
                }
            }
        }
        return grid;
    }

    public GridCell[][] usingEuclideanDistance(GridCell[][] grid, GridCell currentCell, int[][] directions, PriorityQueue<GridCell> pq) {
        //The next loop enable the robot to move in eight directions (using Euclidean distance)
        for (int dr = -1; dr <= 1; dr += 1) { // Consider only up (row - 1) and down (row + 1)
            for (int dc = -1; dc <= 1; dc += 1) { // Consider only left (col - 1) and right (col + 1)
                if (dr == 0 && dc == 0) continue;
                int newRow = currentCell.row + dr;
                int newCol = currentCell.col + dc;

                if (isValid(newRow, newCol, grid) && !grid[newRow][newCol].isObstacle && !grid[newRow][newCol].isCurrent) {
                    int newDistance = currentCell.distance + 1;
                    if (newDistance < grid[newRow][newCol].distance) {
                        grid[newRow][newCol].distance = newDistance;
                        grid[newRow][newCol].parent = currentCell;
                        pq.add(grid[newRow][newCol]);
                    }
                }
            }
        }
        return grid;
    }

    public GridCell findNearestChargingStation(GridCell[][] grid) {
        PriorityQueue<GridCell> pq = new PriorityQueue<>(Comparator.comparingInt(cell -> cell.distance));
        grid[currentRow][currentCol].distance = 0;
        pq.add(grid[currentRow][currentCol]);

        while (!pq.isEmpty()) {
            GridCell currentCell = pq.poll();
            if (currentCell.isChargingStation) {
                return currentCell; // Found the nearest charging station
            }

            usingManhattanDistance(grid, currentCell, directions, pq);
        }

        return null; // No charging station found
    }

    public List<GridCell> returnShortestPath(GridCell chargingStation) {
        if (chargingStation != null) {
            return reconstructPath(chargingStation);
        } else {
            return new ArrayList<>(); // No path to any charging station found
        }
    }

    private boolean isValid(int row, int col, GridCell[][] grid) {
        return row >= 0 && row < grid.length && col >= 0 && col < grid[0].length;
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
