import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class SimpleTrie {
    public static void main(String[] args) {
        SimpleTrie st = new SimpleTrie(new String[] { "bull", "bid", "ball" });
        System.out.println(st.contains("bull"));
        System.out.println(st.contains("bll"));
        st.insert("bll");
        System.out.println(st.contains("bll"));
        st.remove("bid");
        System.out.println(st.contains("bid"));
        st.insert("pen");
        System.out.println(st.contains("pen"));
        System.out.println(st.contains("penn"));
        st.display();
    }

    public SimpleTrie(String[] input) {
        create(input);
    }

    public SimpleTrie() {
        root = new Node(' ');
    }

    private Node root;

    public void create(String[] input) {
        root = new Node(' ');
        for (String s : input) {
            add(s);
        }
    }

    public boolean contains(String s) {
        if (root == null) return false;
        Node curr = root;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            Node next = curr.contains(c);
            if (next != null) {
                curr = next;
            } else {
                return false;
            }
        }
        return true;
    }

    public void insert(String s) {
        if (s == null || s.isEmpty()) return;
        add(s.toLowerCase());
    }

    /**
     * Insert a string and get top matches up to max
     * @param s String to insert
     * @param max Max amount of hits to return
     * @return Hits matching the given string
     */
    public List<String> insertAndGetMatches(String s, int max) {
        if (s == null || s.isEmpty()) return new ArrayList<>();
        Node node = add(s);
        Queue<Match> pq = new PriorityQueue<>(); // PQ to store the all the hits in
        for (int i = 0; i < node.children.length; i++) {
            if (node.children[i] != null) {
                getSubtree(node.children[i], s + node.children[i].key, pq);
            }
        }
        List<String> results = new ArrayList<>();
        for (int i = 0; i < max; i++) {
            if (pq.isEmpty()) break;
            results.add(pq.poll().word);
        }
        return results;
    }

    private void getSubtree(Node root, String s, Queue<Match> q) {
        if (root.end) q.add(new Match(s, root.hits));
        for (int i = 0; i < root.children.length; i++) {
            if (root.children[i] != null) {
                getSubtree(root.children[i], s + root.children[i].key, q);
            }
        }
    }

    private Node add(String s) {
        Node curr = root;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            Node next = curr.contains(c);
            if (next != null) {
                curr = next;
            } else {
                curr = curr.insert(c);
            }
        }
        curr.end = true;
        return curr;
    }

    public void remove(String s) {
        if (root == null) return;
        Node parent = null;
        Node curr = root;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            Node next = curr.contains(c);
            if (next != null) {
                parent = curr;
                if (next.count <= 1) {
                    parent.remove(c);
                }
                curr = next;
            } else {
                return;
            }
        }
    }

    public void display() {
        displaySubtree(root, "");
    }

    private void displaySubtree(Node root, String s) {
        if (root.end) System.out.println(s);
        for (int i = 0; i < root.children.length; i++) {
            if (root.children[i] != null) {
                displaySubtree(root.children[i], s + root.children[i].key);
            }
        }
    }
}

class Node {
    public char key;
    public boolean end; // Denote that this node is the end of a word.
    public Node[] children;
    public byte count; // Number of children
    public int hits; // Number times this node has been used

    public Node(char key) {
        this.key = key;
        this.children = new Node[26];
    }

    public Node contains(char key) {
        hits++;
        return children[range(key)];
    }

    public Node insert(char key) {
        Node node = new Node(key);
        children[range(key)] = node;
        count++;
        return node;
    }

    public void remove(char key) {
        children[range(key)] = null;
        count--;
    }

    private int range(char key) { // Transform the character in to the range of 0-26 (a-z)
        return (int)key - 97;
    }
}

class Match implements Comparable<Match> {
    public String word;
    private int hits;

    public Match(String word, int hits) {
        this.word = word;
        this.hits = hits;
    }

    @Override
    public int compareTo(Match other) {
        return Integer.compare(hits, other.hits);
    }
}