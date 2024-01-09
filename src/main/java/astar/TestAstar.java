package astar;

import common.Statistics;
import grid.GridCell;
import grid.GridEnvironment;
import java.util.*;

@SuppressWarnings({"squid:S106", "PMD.SystemPrintln"}) // System.out is OK in this test program
public class TestAstar {

    private static final int MAX_WARMUPS = 1;
    private static final int MAX_ITERATIONS = 100;

    private static final int MIN_ROWS = 100;
    private static final int MAX_ROWS = 500;
    private static final int MIN_COLS = 100;
    private static final int MAX_COLS = 500;

    private static final Map<Integer, List<Long>> TIMES = new HashMap<>();

    private static int blackhole = 0;

    public static void main(String[] args) {
        Robot robot = new Robot(4, 4, 100); // Example initial position and battery level.

        for (int i = 0; i < MAX_WARMUPS; i++) {
            runTests(i, true, robot);
        }
        for (int i = 0; i < MAX_ITERATIONS; i++) {
            runTests(i, false, robot);
        }
    }

    private static void runTests(int iteration, boolean warmup, Robot robot) {
        System.out.printf("%n%sIteration %d:%n", warmup ? "Warmup - " : "Test - ", iteration + 1);
        for (int numRows = MIN_ROWS; numRows <= MAX_ROWS; numRows += 100) {
            for (int numCols = MIN_COLS; numCols <= MAX_COLS; numCols += 100) {
                long time = runTestForGridSize(numRows, numCols, robot);
                System.out.printf(
                        Locale.US,
                        "Time for grid with %,4d rows and %,4d cols = %,8.1f ms",
                        numRows,
                        numCols,
                        time / 1_000_000.0);

                if (!warmup) {
                    List<Long> times = TIMES.computeIfAbsent(numRows * numCols, k -> new ArrayList<>());
                    times.add(time);
                    long median = Statistics.median(times);
                    System.out.printf(
                            Locale.US,
                            "  -->  Median after %2d iterations = %,8.1f ms",
                            iteration + 1,
                            median / 1_000_000.0);
                }
                System.out.println();
            }
        }
        System.out.println("blackhole = " + blackhole);
    }

    private static long runTestForGridSize(int numRows, int numCols, Robot robot) {
        GridCell shortestPath = null;
        long time = 0;
        while (shortestPath == null) {
            GridEnvironment gridEnvironment = new GridEnvironment(numRows, numCols);
            // Add obstacles and charging stations randomly
            gridEnvironment.addMultiObstacles(6);
            gridEnvironment.addMultiChargingStation(5);
            time = System.nanoTime();
            shortestPath = robot.findNearestChargingStation(gridEnvironment.gridCells);
            System.out.println("Nearest Charging Station = " + "[" + shortestPath.row + "]" + "[" + shortestPath.col + "]");
            time = System.nanoTime() - time;
        }
//        blackhole += shortestPath.size();
        return time;
    }
}
