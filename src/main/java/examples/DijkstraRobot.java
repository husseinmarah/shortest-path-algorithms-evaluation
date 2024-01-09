package examples;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

public class DijkstraRobot {
    private static final int INF = Integer.MAX_VALUE;
    public static int[][] findOptimalChargingStations(int[][] grid, int startX, int startY) {
        int rows = grid.length;
        int cols = grid[0].length;

        int[][] distances = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            Arrays.fill(distances[i], INF);
        }

        distances[startX][startY] = 0;

        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(node -> node.distance));
        System.out.println("priorityQueue = " + priorityQueue);
        priorityQueue.offer(new Node(startX, startY, 0));

        while (!priorityQueue.isEmpty()) {
            Node currentNode = priorityQueue.poll();
            int x = currentNode.x;
            int y = currentNode.y;

            // Check neighbors (up, down, left, right)
            int[] dx = {-1, 1, 0, 0};
            int[] dy = {0, 0, -1, 1};

            for (int i = 0; i < 4; i++) {
                int newX = x + dx[i];
                int newY = y + dy[i];

                if (newX >= 0 && newX < rows && newY >= 0 && newY < cols && grid[newX][newY] != 1) {
                    int newDistance = distances[x][y] + 1; // Assuming equal cost for each move

                    if (newDistance < distances[newX][newY]) {
                        distances[newX][newY] = newDistance;
                        priorityQueue.offer(new Node(newX, newY, newDistance));
                    }
                }
            }
        }

        return distances;
    }

    public static void main(String[] args) {
        int[][] grid = {
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 1, 0, 0, 1, 1, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 1, 0},
                {0, 0, 1, 1, 1, 0, 0, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 1, 0},
                {0, 0, 0, 1, 1, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 1, 1, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        };

        int startX = 2;
        int startY = 2;

        int[][] result = findOptimalChargingStations(grid, startX, startY);

        System.out.println("Distances from the starting point:");
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                System.out.print(result[i][j] == INF ? "INF" : result[i][j]);
                System.out.print("\t");
            }
            System.out.println();
        }
    }
}

class Node {
    int x;
    int y;
    int distance;

    Node(int x, int y, int distance) {
        this.x = x;
        this.y = y;
        this.distance = distance;
    }
}


//public class DijkstraRobot {
//
//        @Benchmark
//    @Fork(value = 1)
//    @BenchmarkMode(Mode.Throughput)
//    @OutputTimeUnit(TimeUnit.MICROSECONDS)
//    @Warmup(iterations = 10, time = 20, timeUnit = TimeUnit.MILLISECONDS)
//    @Measurement(iterations = 20, time = 200, timeUnit = TimeUnit.MILLISECONDS)
//    public void init() {
//        // Do nothing
//        test();
//    }
//
//    public void test(){
//        for (int i = 0; i < 1000; i++) {
//            System.out.println("i = " + i);
//        }
//
//    }

//    @Benchmark
//    @Fork(value = 1)
//    @BenchmarkMode(Mode.Throughput)
//    @OutputTimeUnit(TimeUnit.MICROSECONDS)
//    @Warmup(iterations = 10, time = 20, timeUnit = TimeUnit.MILLISECONDS)
//    @Measurement(iterations = 20, time = 200, timeUnit = TimeUnit.MILLISECONDS)

//    @Benchmark
//    @OutputTimeUnit(TimeUnit.NANOSECONDS)
//    @BenchmarkMode(Mode.AverageTime)
//    public void doNothing() {
//    }
//
//    @Benchmark
//    @OutputTimeUnit(TimeUnit.NANOSECONDS)
//    @BenchmarkMode(Mode.AverageTime)
//    public void objectCreation() {
//        new Object();
//    }
//}