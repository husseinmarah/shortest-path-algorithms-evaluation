package grid;

import bellman.Robot;

import java.util.Objects;
import java.util.Random;

public class GridEnvironment {
    public static GridCell[][] gridCells;
    private int numRows;
    private int numCols;

    public GridEnvironment(int numRows, int numCols) {
        this.numRows = numRows;
        this.numCols = numCols;
        // Initialize the gridCells with the specified number of rows and columns
        gridCells = new GridCell[numRows][numCols];

        // Initialize each cell in the gridCells
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                gridCells[i][j] = new GridCell(i, j, false, false, false);
            }
        }
    }
    public void setCurrentPosition(Robot robot){
        if(Objects.nonNull(Robot.getCurrentCell())) {
            int tempRow = Robot.getCurrentCell().row;
            int tempCol = Robot.getCurrentCell().col;
            gridCells[tempRow][tempCol] = new GridCell(tempRow, tempCol, false, false, true);
        }
    }

    public static GridEnvironment buildGrid(int numRows, int numCols) {
        GridEnvironment grid = new GridEnvironment(numRows, numCols);
        // Add obstacles and charging stations randomly
        grid.addRandomObstacles();
        grid.addRandomChargingStations();

        return grid;
    }

    public void addObstacle(int row, int col) {
        // Add an obstacle at the specified position
        if (isValidPosition(row, col) && !gridCells[row][col].isChargingStation || !gridCells[row][col].isCurrent || !gridCells[row][col].isObstacle) {
            gridCells[row][col].isObstacle = true;
        }
    }


    public void addRandomObstacles() {
        for (int i = 0; i < 30 * 30 / 10; i++) {
            int randomRow = (int) (Math.random() * 30);
            int randomCol = (int) (Math.random() * 30);
            addObstacle(randomRow, randomCol);
        }
    }

    public void addRandomChargingStations() {
        for (int i = 0; i < 30 * 30 / 10; i++) {
            int randomRow = (int) (Math.random() * 30);
            int randomCol = (int) (Math.random() * 30);
            addChargingStation(randomRow, randomCol);
        }
    }

    public void addChargingStation(int row, int col) {
        // Add a charging station at the specified position
        if (isValidPosition(row, col) && !gridCells[row][col].isObstacle && !gridCells[row][col].isCurrent && !gridCells[row][col].isChargingStation) {
            gridCells[row][col].isChargingStation = true;
        }
    }

    public void addMultiObstacles(int numObstacles) {
        Random random = new Random();
        for (int i = 0; i < numObstacles; i++) {
            int randomRow = random.nextInt(numRows);
            int randomCol = random.nextInt(numCols);
            addObstacle(randomRow, randomCol);
        }
    }

    public void addMultiChargingStation(int numObstacles) {
        Random random = new Random();
        for (int i = 0; i < numObstacles; i++) {
            int randomRow = random.nextInt(numRows);
            int randomCol = random.nextInt(numCols);
            addChargingStation(randomRow, randomCol);
        }
    }

    private boolean isValidPosition(int row, int col) {
        // Check if the position is within the gridCells bounds
        return row >= 0 && row < gridCells.length && col >= 0 && col < gridCells[0].length;
    }

    public int getNumRows() {
        return numRows;
    }

    public int getNumCols() {
        return numCols;
    }

    public void print() {
        // Print the gridCells for visualization
        for (int i = 0; i < gridCells.length; i++) {
            for (int j = 0; j < gridCells[0].length; j++) {
                if (gridCells[i][j].isObstacle) {
                    System.out.print("X "); // X represents an obstacle
                } else if (gridCells[i][j].isChargingStation) {
                    System.out.print("C "); // C represents a charging station
                } else {
                    System.out.print(". "); // . represents an empty cell
                }
            }
            System.out.println(); // Move to the next row
        }
    }


}