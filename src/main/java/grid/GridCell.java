package grid;

public class GridCell {

    private int INF = Integer.MAX_VALUE;
    public int row;
    public int col;
    public boolean isObstacle;
    public boolean isChargingStation;
    public boolean isVisited;
    public boolean isCurrent;
    public int distance;
    public GridCell parent;

    public GridCell(int row, int col, boolean isObstacle, boolean isChargingStation, boolean isCurrent) {
        this.row = row;
        this.col = col;
        this.isObstacle = isObstacle;
        this.isChargingStation = isChargingStation;
        this.isCurrent = isCurrent;
        this.isVisited = false;
        this.distance = INF;
        this.parent = null;
    }

}