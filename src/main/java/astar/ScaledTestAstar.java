package astar;

import common.CSVFile;
import common.Statistics;
import grid.GridCell;
import grid.GridEnvironment;

import java.io.IOException;
import java.util.*;

@SuppressWarnings({"squid:S106", "PMD.SystemPrintln"}) // System.out is OK in this test program
//@SuppressWarnings({"squid:S106", "PMD.SystemPrintln"}) // System.out is OK in this test program
public class ScaledTestAstar {

    private static final int MAX_WARMUPS = 50;
    private static final int MAX_ITERATIONS = 10;

    private static final int MIN_ROWS = 10;
    private static int MAX_ROWS = 1000;
    private static final int MIN_COLS = 10;
    private static int MAX_COLS = 1000;

    private static final Map<Integer, List<Long>> TIMES = new HashMap<>();

    private static int blackhole = 0;

    public static void main(String[] args) throws IOException {
        CSVFile.createCSVFile(ScaledTestAstar.class.getPackageName());
        Robot robot = new Robot(0, 0, 100); // Example initial position and battery level.
        for (int k = 0; k < MAX_WARMUPS; k++) {
            // Define the initial number of rows and columns
            int numRows = 0;
            int numCols = 0;

            // Define the number of iterations for the first phase (increasing by 10)
            int numIterationsPhase1 = 10;

            // Define the number of iterations for the second phase (increasing by 100)
            int numIterationsPhase2 = 10;

            // Iterate through the grid
            for (int i = 1; i <= numIterationsPhase1; i++) {
                numRows += 10;
                numCols += 10;
                runTests(i, true, robot, numRows, numCols);
            }

            for (int i = 1; i <= numIterationsPhase2; i++) {
                runTests(i, true, robot, numRows, numCols);
                numRows += 100;
                numCols += 100;
            }
        }
        for (int k = 0; k < MAX_ITERATIONS; k++) {
            // Define the initial number of rows and columns
            int numRows = 0;
            int numCols = 0;

            // Define the number of iterations for the first phase (increasing by 10)
            int numIterationsPhase1 = 10;

            // Define the number of iterations for the second phase (increasing by 100)
            int numIterationsPhase2 = 10;

            // Iterate through the grid
            for (int i = 1; i <= numIterationsPhase1; i++) {
                numRows += 10;
                numCols += 10;
                runTests(i, false, robot, numRows, numCols);
            }

            for (int i = 1; i <= numIterationsPhase2; i++) {
                runTests(i, false, robot, numRows, numCols);
                numRows += 100;
                numCols += 100;
            }
        }

        CSVFile.closeCSVFile();

    }

    private static void runTests(int iteration, boolean warmup, Robot robot, int numRows, int numCols) throws IOException {
        System.out.printf("%n%sIteration %d:%n", warmup ? "Warmup - " : "Test - ", iteration + 0);
        for (int row = 1; row <= numRows; row += numRows) {
            for (int col = 1; col <= numCols; col += numCols) {
                List<Long> resultTime = runTestForGridSize(numRows, numCols, robot);
                long time = resultTime.get(0);
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
                    long totalTraversalTime= resultTime.get(1);
                    System.out.printf(
                            Locale.US,
                            "  --> After %2d iterations, Median = %,8.1f ms, Average = %,8.1f ms",
                            iteration + 1,
                            median / 1_000_000.0, average / 1_000_000.0);
                    String[] csvData = {String.valueOf(numRows), String.valueOf(numCols), String.valueOf(numRows * numCols), String.valueOf(iteration), String.valueOf(median / 1_000_000.0), String.valueOf(average / 1_000_000.0), String.valueOf(totalTraversalTime / 1_000_000.0)};
                    CSVFile.updateCSV(csvData);
                }
            }
        }
        System.out.println("blackhole = " + blackhole);
    }

    private static List<Long> runTestForGridSize(int numRows, int numCols, Robot robot) {
        GridCell shortestPath = null;
        long time = 0;
        List<Long> times = new ArrayList<>(); // Create an ArrayList object
        while (shortestPath == null) {
            GridEnvironment gridEnvironment = getGridEnvironment(numRows, numCols);
            GridCell destination = new GridCell(numRows - 1, numCols - 1, false, false, false);
            time = System.nanoTime();
            shortestPath = robot.findDestinationStation(gridEnvironment.gridCells, destination);
            System.out.println("Destination Station = " + "[" + shortestPath.row + "]" + "[" + shortestPath.col + "]");
            time = System.nanoTime() - time;

            times.add(time);
            times.add(calculateGridTraversalTime(gridEnvironment.gridCells));

        }
//        blackhole += shortestPath.size();
        return times;
    }

    private static long calculateGridTraversalTime(GridCell[][] grid) {
        long startTime = System.nanoTime();

        // Traverse all cells
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j].isObstacle) {
//                    System.out.println("Obstacle: [" + i + "][" + j + "]");
                } else if (grid[i][j].isChargingStation) {
//                    System.out.println("Charging Station: [" + i + "][" + j + "]");
                }

            }
        }
        // Stop measuring time
        long stopTime = System.nanoTime();
        long elapsedTime = stopTime - startTime;
        double elapsedTimeMillis = (double) elapsedTime / 1_000_000; // Convert nanoseconds to milliseconds
        System.out.println("Total time taken to traverse the grid: " + elapsedTimeMillis + " milliseconds");
        return elapsedTime;
    }

    private static GridEnvironment getGridEnvironment(int numRows, int numCols) {
        GridEnvironment gridEnvironment = new GridEnvironment(numRows, numCols);
        // Add obstacles and charging stations
//        gridEnvironment.addObstacle(1, 6);
//        gridEnvironment.addObstacle(1, 3);
//        gridEnvironment.addObstacle(3, 4);
//        gridEnvironment.addObstacle(4, 1);
//        gridEnvironment.addObstacle(4, 7);
//        gridEnvironment.addObstacle(6, 5);
//        gridEnvironment.addObstacle(6, 9);
//        gridEnvironment.addObstacle(8, 2);
//        gridEnvironment.addObstacle(9, 6);
//        gridEnvironment.addChargingStation(1, 1);
//        gridEnvironment.addChargingStation(2, 7);
//        gridEnvironment.addChargingStation(6, 2);

        if (numRows > 1 && numCols > 1) {
            // Assign obstacles in the environment randomly by considering the number of rows and columns
            // Add a proportional number of obstacles to the total environment
            int numberObstacles = (int) Math.round(((numRows * numRows) * 0.025) * 0.30);
            System.out.println("numberObstacles = " + numberObstacles);
            // Add a proportional number of obstacles to the total environment
            int numberChargingStations = (int) Math.round(((numRows * numRows) * 0.025) * 0.10);
            System.out.println("numberChargingStations = " + numberChargingStations);

            // Add obstacles and charging stations in random places
//        gridEnvironment.addMultiObstacles(6);
//        gridEnvironment.addMultiChargingStation(5);


            // add random obstacles
            Random random = new Random();
            for (int i = 0; i < numberObstacles; i++) {
                int randomRow = random.nextInt(numRows - 2);
                int randomCol = random.nextInt(numCols - 2);
                gridEnvironment.addObstacle(randomRow, randomCol);
            }

            // add random charging stations
            for (int i = 0; i < numberChargingStations; i++) {
                int randomRow = random.nextInt(numRows - 2);
                int randomCol = random.nextInt(numCols - 2);
                gridEnvironment.addChargingStation(randomRow, randomCol);
            }
        }

        return gridEnvironment;
    }
}
