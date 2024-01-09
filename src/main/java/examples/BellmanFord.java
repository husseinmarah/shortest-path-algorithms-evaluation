package examples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BellmanFord {

    public static class GridCell {
        int row, col;

        public GridCell(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }

    public static class Edge {
        GridCell source, destination;
        int weight;

        public Edge(GridCell source, GridCell destination, int weight) {
            this.source = source;
            this.destination = destination;
            this.weight = weight;
        }
    }

    public static class Result {
        int[] distance;
        GridCell[] predecessor;

        public Result(int size) {
            distance = new int[size];
            predecessor = new GridCell[size];
        }
    }

    public static Result bellmanFord(List<GridCell> vertices, List<Edge> edges, GridCell source) {
        int numVertices = vertices.size();
        Result result = new Result(numVertices);

        // Step 1: initialize graph
        for (GridCell vertex : vertices) {
            // Initialize the distance to all vertices to infinity
            result.distance[vertex.row * numVertices + vertex.col] = Integer.MAX_VALUE;
            // And having a null predecessor
            result.predecessor[vertex.row * numVertices + vertex.col] = null;
        }

        // The distance from the source to itself is, of course, zero
        result.distance[source.row * numVertices + source.col] = 0;

        // Step 2: relax edges repeatedly
        for (int i = 0; i < numVertices - 1; i++) {
            for (Edge edge : edges) {
                GridCell u = edge.source;
                GridCell v = edge.destination;
                int w = edge.weight;

                int uIndex = u.row * numVertices + u.col;
                int vIndex = v.row * numVertices + v.col;

                if (result.distance[uIndex] != Integer.MAX_VALUE && result.distance[uIndex] + w < result.distance[vIndex]) {
                    result.distance[vIndex] = result.distance[uIndex] + w;
                    result.predecessor[vIndex] = u;
                }
            }
        }

        // Step 3: check for negative-weight cycles
        for (Edge edge : edges) {
            GridCell u = edge.source;
            GridCell v = edge.destination;
            int w = edge.weight;

            int uIndex = u.row * numVertices + u.col;
            int vIndex = v.row * numVertices + v.col;

            if (result.distance[uIndex] != Integer.MAX_VALUE && result.distance[uIndex] + w < result.distance[vIndex]) {
                result.predecessor[vIndex] = u;
                // A negative cycle exists; find a vertex on the cycle
                boolean[] visited = new boolean[numVertices * numVertices];
                visited[vIndex] = true;

                while (!visited[uIndex]) {
                    visited[uIndex] = true;
                    u = result.predecessor[uIndex];
                    uIndex = u.row * numVertices + u.col;
                }

                // u is a vertex in a negative cycle, find the cycle itself
                List<GridCell> negativeCycle = new ArrayList<>();
                negativeCycle.add(u);

                v = result.predecessor[uIndex];
                vIndex = v.row * numVertices + v.col;

                while (vIndex != uIndex) {
                    negativeCycle.add(v);
                    v = result.predecessor[vIndex];
                    vIndex = v.row * numVertices + v.col;
                }

                throw new RuntimeException("Graph contains a negative-weight cycle: " + negativeCycle);
            }
        }

        return result;
    }

    // Sample usage
    public static void main(String[] args) {
        int numRows = 3;
        int numCols = 3;

        // Create a grid of GridCell objects
        List<GridCell> vertices = new ArrayList<>();
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                vertices.add(new GridCell(i, j));
            }
        }

        // Create edges with weights (assuming uniform weight for simplicity)
        List<Edge> edges = new ArrayList<>();
        edges.add(new Edge(vertices.get(0), vertices.get(1), 1));
        edges.add(new Edge(vertices.get(1), vertices.get(2), -1));
        edges.add(new Edge(vertices.get(2), vertices.get(0), 2));

        // Set the source cell
        GridCell source = vertices.get(0);

        // Run Bellman-Ford algorithm
        try {
            Result result = bellmanFord(vertices, edges, source);

            // Print the result
            System.out.println("Shortest distances:");
            for (GridCell vertex : vertices) {
                int index = vertex.row * numCols + vertex.col;
                System.out.println("(" + vertex.row + ", " + vertex.col + "): " + result.distance[index]);
            }
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }
}
