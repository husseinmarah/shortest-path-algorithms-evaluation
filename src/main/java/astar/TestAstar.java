package astar;

import bellman.TestBellman;
import common.CSVFile;
import common.Statistics;
import grid.GridCell;
import grid.GridEnvironment;

import java.io.IOException;
import java.util.*;

@SuppressWarnings({"squid:S106", "PMD.SystemPrintln"}) // System.out is OK in this test program
//@SuppressWarnings({"squid:S106", "PMD.SystemPrintln"}) // System.out is OK in this test program
public class TestAstar {

    private static final int MAX_WARMUPS = 1;
    private static final int MAX_ITERATIONS = 10;

    private static final int MIN_ROWS = 1000;
    private static final int MAX_ROWS = 5000;
    private static final int MIN_COLS = 1000;
    private static final int MAX_COLS = 5000;

    private static final Map<Integer, List<Long>> TIMES = new HashMap<>();

    private static int blackhole = 0;

    public static void main(String[] args) throws IOException {
        CSVFile.createCSVFile(TestAstar.class.getPackageName());
        Robot robot = new Robot(4, 4, 100); // Example initial position and battery level.
        for (int i = 0; i < MAX_WARMUPS; i++) {
            runTests(i, true, robot);
        }
        for (int i = 0; i < MAX_ITERATIONS; i++) {
            runTests(i, false, robot);
        }
        CSVFile.closeCSVFile();

    }

    private static void runTests(int iteration, boolean warmup, Robot robot) throws IOException {
        System.out.printf("%n%sIteration %d:%n", warmup ? "Warmup - " : "Test - ", iteration + 1);
        for (int numRows = MIN_ROWS; numRows <= MAX_ROWS; numRows += 1000) {
            for (int numCols = MIN_COLS; numCols <= MAX_COLS; numCols += 1000) {
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
                    long average = Statistics.average(times);
                    System.out.printf(
                            Locale.US,
                            "  --> After %2d iterations, Median = %,8.1f ms, Average = %,8.1f ms",
                            iteration + 1,
                            median / 1_000_000.0, average / 1_000_000.0);
                    String[] csvData = {String.valueOf(numRows), String.valueOf(numCols), String.valueOf(numRows * numCols), String.valueOf(iteration), String.valueOf(median / 1_000_000.0), String.valueOf(average / 1_000_000.0)};
                    CSVFile.updateCSV(csvData);
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
            GridCell destination = new GridCell(28, 29, false,false, false);
            shortestPath = robot.findDestinationStation(gridEnvironment.gridCells, destination);
            System.out.println("Destination Station = " + "[" + shortestPath.row + "]" + "[" + shortestPath.col + "]");
            time = System.nanoTime() - time;
        }
//        blackhole += shortestPath.size();
        return time;
    }
}
