package astar;

import grid.GridCell;

import java.util.*;

class Robot {
    int batteryLevel;
    int currentRow, currentCol;
    static int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // Possible movement directions (up, down, left, right)

    public Robot(int currentRow, int currentCol, int batteryLevel) {
        this.currentRow = currentRow;
        this.currentCol = currentCol;
        this.batteryLevel = batteryLevel;
    }


        public void usingAStar(GridCell[][] grid, GridCell currentCell, PriorityQueue<GridCell> pq) {
            // This loop enables the robot to move only in four directions (using Manhattan distance)
            for (int[] dir : directions) {
                int newRow = currentCell.row + dir[0];
                int newCol = currentCell.col + dir[1];

                if (isValid(newRow, newCol, grid) && !grid[newRow][newCol].isObstacle) {
                    int newDistance = currentCell.distance + 1;
                    GridCell neighbor = grid[newRow][newCol];

                    if (newDistance < neighbor.distance) {
                        neighbor.distance = newDistance;
                        neighbor.parent = currentCell;
                        pq.add(neighbor);
                    }
                }
            }
        }

    public int calculateHeuristic(GridCell cell, GridCell[][] grid) {
        int minDistance = Integer.MAX_VALUE;

        // Iterate through the grid to find the nearest unvisited cell
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (!grid[i][j].isVisited) {
                    int distance = Math.abs(cell.row - i) + Math.abs(cell.col - j);
                    minDistance = Math.min(minDistance, distance);
                }
            }
        }

        return minDistance;
    }


    public GridCell findNearestChargingStation(GridCell[][] grid) {
        PriorityQueue<GridCell> pq = new PriorityQueue<>(Comparator.comparingInt(cell ->
                cell.distance + calculateHeuristic(cell, grid)));
        grid[currentRow][currentCol].distance = 0;
        pq.add(grid[currentRow][currentCol]);

        while (!pq.isEmpty()) {
            GridCell currentCell = pq.poll();
            if (currentCell.isChargingStation) {
                return currentCell; // Found the nearest charging station
            }

            usingAStar(grid, currentCell, pq);

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
