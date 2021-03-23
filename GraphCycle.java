import java.util.Arrays;
import java.util.List;

/**
 * Find a cycle in a graph using union find data structure
 */
public class GraphCycle {
    public static void main(String[] args) {
        List<Edge> edges = Arrays.asList(
            new Edge(1, 2), new Edge(1, 7), new Edge(1, 8),
            new Edge(2, 3), new Edge(2, 6), new Edge(3, 4),
            new Edge(3, 5), new Edge(8, 9), new Edge(8, 12),
            new Edge(9, 10), new Edge(9, 11), new Edge(11, 12) // 11, 12 introduces a cycle to the graph
        );
        GraphCycle gc = new GraphCycle();
        System.out.println(gc.compute(edges, 12));
    }

    public boolean compute(List<Edge> edges, int length) {
        UnionFind uf = new UnionFind(UnionFind.toValues(length));
        for (Edge edge : edges) {
            int x = uf.find(edge.src);
            int y = uf.find(edge.dest);
            if (x == y) return true;
            uf.union(x, y);
        }
        return false;
    }
}


class Edge {
    public int src, dest;
    public Edge(int src, int dest) {
        this.src = src;
        this.dest = dest;
    }
}