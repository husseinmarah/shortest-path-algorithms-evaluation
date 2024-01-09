package examples;

import java.util.Comparator;
import java.util.PriorityQueue;

public class ShortestPath {
    static int[][] grid = new int[10][10]; // Your grid with obstacles and charging stations
    static int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // Possible movement directions (up, down, left, right)

    // Helper function to check if a cell is valid (within bounds and not an obstacle)
    static boolean isValid(int x, int y) {
        return x >= 0 && x < 10 && y >= 0 && y < 10 && grid[x][y] != 1;
    }


    // Dijkstra's algorithm to find the shortest path

    public static int dijkstra(int[] start, int[] end) {
        PriorityQueue<int[]> queue = new PriorityQueue<>(Comparator.comparingInt(a -> a[2]));
        queue.offer(new int[]{start[0], start[1], 0});
        boolean[][] visited = new boolean[10][10];

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int x = current[0];
            int y = current[1];
            int dist = current[2];

            if (x == end[0] && y == end[1]) {
                return dist; // Found the charging station
            }

            if (!visited[x][y]) {
                visited[x][y] = true;

                for (int[] dir : directions) {
                    int newX = x + dir[0];
                    int newY = y + dir[1];

                    if (isValid(newX, newY)) {
                        queue.offer(new int[]{newX, newY, dist + 1});
                    }
                }
            }
        }

        return -1; // No path found to charging station
    }

    public static void main(String[] args) {
        int[] robotPosition = {0, 0}; // Replace with robot's current position
        int[] chargingStation = {5, 7}; // Replace with the coordinates of charging station

        int shortestDistance = dijkstra(robotPosition, chargingStation);

        if (shortestDistance == -1) {
            System.out.println("No path to the charging station.");
        } else {
            System.out.println("Shortest distance to the charging station: " + shortestDistance);
        }
    }
}
