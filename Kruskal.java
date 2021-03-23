import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Kruskal {
    public static void main(String[] args) {
        List<Edge> edges = Arrays.asList(
            new Edge(0, 1, 7), new Edge(1, 2, 8),
            new Edge(0, 3, 5), new Edge(1, 3, 9),
            new Edge(1, 4, 7), new Edge(2, 4, 5),
            new Edge(3, 4, 15), new Edge(3, 5, 6),
            new Edge(4, 5, 8), new Edge(4, 6, 9),
            new Edge(5, 6, 11)
        );
        Kruskal k = new Kruskal();
        List<Edge> mst = k.compute(edges, 7);
        k.printGraph(mst);
        edges = Arrays.asList(
            new Edge(0, 1, 8), new Edge(1, 0, 4),
            new Edge(1, 2, 4), new Edge(2, 1, 5),
            new Edge(0, 4, 7), new Edge(4, 5, 2),
            new Edge(5, 4, 10)
        );
        mst = k.compute(edges, 5);
        k.printGraph(mst);
    }

    /**
     * Compure minimum spanning tree for the given graph and given length (size) of the graph
     * @param edges Adjacency list representation of the graph's edges, with source, destination and weight of the edge
     * @param length Length (size) of the graph, or in other words the number of vertices in the graph
     * @return Minimum spanning tree for this graph
     */
    public List<Edge> compute(List<Edge> edges, int length) {
        Collections.sort(edges, Comparator.comparingInt(edge -> edge.weight)); // Sort edges by weight
        List<Edge> current = new ArrayList<>(); // Current edges in the mst
        UnionFind uf = new UnionFind(UnionFind.toValues(length)); // Union find data structure to check for which edges are in our spanning tree, and detecting cycles
        int ind = 0; // Current edge index
        while (current.size() < (length - 1)) { // While list does not contain the spanning tree.. (number of edges - 1)
            Edge next = edges.get(ind++);
            int x = uf.find(next.src);
            int y = uf.find(next.dest);
            if (x != y) { // Make sure edges don't belong in the same set, as that would mean a cycle
                current.add(next);
                uf.union(x, y); // Edge is now included in our spanning tree
            }
        }
        return current;
    }

    private void printGraph(List<Edge> edges) {
        System.out.println();
        for (Edge edge : edges) {
            System.out.println(edge.src + "-" + edge.dest);
        }
    }
}

class Edge {
    public int src, dest, weight;
    public Edge(int src, int dest, int weight) {
        this.src = src;
        this.dest = dest;
        this.weight = weight;
    }
}