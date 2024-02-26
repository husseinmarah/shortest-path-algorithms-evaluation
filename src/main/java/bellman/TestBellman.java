package bellman;

import common.CSVFile;
import common.Statistics;
import grid.GridCell;
import grid.GridEnvironment;

import java.io.IOException;
import java.util.*;

/**
*@author <a href="https://github.com/husseinmarah">Hussein Marah</a>
*/

//@SuppressWarnings({"squid:S106", "PMD.SystemPrintln"}) // System.out is OK in this test program
public class TestBellman {

private static final int MAX_WARMUPS = 5;
private static final int MAX_ITERATIONS = 10;

private static final int MIN_ROWS = 1000;
private static final int MAX_ROWS = 5000;
private static final int MIN_COLS = 1000;
private static final int MAX_COLS = 5000;

private static final Map<Integer, List<Long>> TIMES = new HashMap<>();
private static int blackhole = 0;

public static void main(String[] args) throws IOException {
    CSVFile.createCSVFile(TestBellman.class.getPackageName());
    Robot robot = new Robot(0, 0, 100); // Example initial position and battery level.

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
    GridCell nearestChargingStation = null;
    long time = 0;
    while (nearestChargingStation == null) {
        GridEnvironment gridEnvironment = new GridEnvironment(numRows, numCols);
        // Add obstacles and charging stations randomly
//        gridEnvironment.addMultiObstacles(6);
//        gridEnvironment.addMultiChargingStation(5);
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

        // Set the source cell
        GridCell source = gridEnvironment.gridCells[robot.currentRow][robot.currentCol];
//        gridEnvironment.setCurrentPosition(robot);

        time = System.nanoTime();
        // Run Bellman-Ford algorithm
        robot.bellmanFord(gridEnvironment.gridCells, source);
        time = System.nanoTime() - time;
        nearestChargingStation = robot.findNearestChargingStation(gridEnvironment.gridCells);
        System.out.println("Current Position: " + "[" + source.row + "]" + "[" + source.col + "]");
//        printGrid(gridEnvironment.gridCells);
        if (nearestChargingStation != null) {
            System.out.println("Nearest Charging Station: " + "[" + nearestChargingStation.row + "]" + "[" + nearestChargingStation.col + "]");
        } else {
            System.out.println("No charging station found.");
        }
    }
    blackhole += nearestChargingStation.distance;
    return time;
}

}
