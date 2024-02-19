package astar;

import grid.GridCell;

import java.util.*;

class Robot {
    int batteryLevel;
    int currentRow, currentCol;
    static int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // Possible movement directions (up, down, left, right)

    GridCell[][] currentPosition;

    public Robot(int currentRow, int currentCol, int batteryLevel) {
        this.currentRow = currentRow;
        this.currentCol = currentCol;
        this.batteryLevel = batteryLevel;
    }

    public void usingAStar(GridCell[][] grid, GridCell currentCell, PriorityQueue<GridCell> pq, GridCell destination) {
        for (int[] dir : directions) {
            int newRow = currentCell.row + dir[0];
            int newCol = currentCell.col + dir[1];

            if (isValid(newRow, newCol, grid) && !grid[newRow][newCol].isObstacle) {
                GridCell neighbor = grid[newRow][newCol];
                int newDistance = currentCell.distance + 1; // Assuming uniform cost

                if (newDistance < neighbor.distance) {
                    neighbor.distance = newDistance;
                    neighbor.parent = currentCell;
                    int heuristic = calculateHeuristic(neighbor, destination);
                    neighbor.heuristic = heuristic;
                    pq.add(neighbor);
                }
            }
        }
    }

    public GridCell getCurrentPosition() {
        return currentPosition[currentCol][currentRow];
    }

    public void setCurrentPosition(int currentCol, int currentRow) {
        this.currentCol = currentCol;
        this.currentRow = currentRow;

    }

    public int calculateHeuristic(GridCell cell, GridCell destination) {
        // Assuming Manhattan distance heuristic
        return Math.abs(cell.row - destination.row) + Math.abs(cell.col - destination.col);
    }

    public GridCell findDestinationStation(GridCell[][] grid, GridCell destination) {
        PriorityQueue<GridCell> pq = new PriorityQueue<>(Comparator.comparingInt(cell ->
                cell.distance + cell.heuristic));
        grid[currentRow][currentCol].distance = 0;
        pq.add(grid[currentRow][currentCol]);

        while (!pq.isEmpty()) {
            GridCell currentCell = pq.poll();
            if (currentCell.col==destination.col && currentCell.row == destination.row) {
                return currentCell; // Found the destination
            }

            usingAStar(grid, currentCell, pq, destination);
        }

        return null; // No charging station found
    }

    public List<GridCell> returnShortestPath(GridCell destination) {
        if (destination != null) {
            return reconstructPath(destination);
        } else {
            return new ArrayList<>(); // No path to any charging station found
        }
    }

    private boolean isValid(int row, int col, GridCell[][] grid) {
        return row >= 0 && row < grid.length && col >= 0 && col < grid[0].length;
    }

    private List<GridCell> reconstructPath(GridCell destination) {
        List<GridCell> path = new ArrayList<>();
        GridCell currentCell = destination;

        while (currentCell != null) {
            path.add(currentCell);
            currentCell = currentCell.parent;
        }

        Collections.reverse(path);
        return path;
    }

}
