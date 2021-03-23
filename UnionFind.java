import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UnionFind {
    public static void main(String[] args) {
        List<Integer> l = new ArrayList<>();
        l.add(1);
        l.add(2);
        l.add(3);
        l.add(4);
        l.add(5);
        UnionFind uf = new UnionFind(l);
        uf.union(1, 2);
        uf.union(1, 3);
    }

    private Map<Integer, Integer> parents;
    private Map<Integer, Integer> ranks;
    /**
     * Number of sets
     */
    public int count;

    public UnionFind(List<Integer> values) {
        parents = new HashMap<>();
        ranks = new HashMap<>();
        createSet(values);
    }

    public void createSet(List<Integer> values) {
        for (Integer i : values) {
            parents.put(i, i);
            ranks.put(i, 0);
        }
        count += values.size();
    }

    public int find(int n) {
        if (parents.get(n) != n) { // Path compression, directly link all nodes to the root
            parents.put(n, find(parents.get(n)));
        }
        return parents.get(n);
    }

    public void union(int x, int y) {
        x = find(x);
        y = find(y);
        if (x == y) return; // In the same set
        if (ranks.get(x) > ranks.get(y)) {
            parents.put(y, x);
        } else if (ranks.get(y) > ranks.get(x)) {
            parents.put(x, y);
        } else {
            parents.put(x, y);
            ranks.put(y, ranks.get(y) + 1);
        }
        count--; // Sets are combined so the total number of sets decreases by one.
    }

    public boolean connected(int x, int y) {
        return find(x) == find(y);
    }

    public static List<Integer> toValues(int n) {
        List<Integer> values = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            values.add(i);
        }
        return values;
    }
}

class Node {
    public int data;
    public int rank;
}