import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * An implementation of a "Israeli Queue", a priority queue where values in the priority queue have an additional 
 * property called groups - as in, the values in the queue are in groups with these groups having their own priorities and "id" or "association".
 * https://rapidapi.com/blog/israeli-queues-exploring-a-bizarre-data-structure/
 */
public class IsraeliQueue<K, V> {
    public static void main(String[] args) {
        IsraeliQueue<Integer, Integer> iq = new IsraeliQueue<>();
        iq.insert(100, 5, 10);
        iq.insert(50, 5, 5);
        iq.insert(50, 7, 12);
        iq.insert(50, 7, 50);
        iq.insert(5435, 64, 1);
        iq.insert(22, 5, 88);
        while (iq.getSize() > 0) {
            Queue<Node<Integer>> values = iq.extract().getValues();
            while (!values.isEmpty()) {
                System.out.println(values.poll().priority);
            }
        }
    }

    private Queue<Group<K, V>> groups;

    public Queue<Group<K, V>> getGroups() {
        return groups;
    }

    public int getSize() {
        return groups.size();
    }

    public IsraeliQueue() {
        groups = new PriorityQueue<>(new Group<>());
    }

    /**
     * Insert a new value in to the queue
     * @param value Value to insert
     * @param id "Identification" or "association" of this value
     * @param priority Priority of this particular item
     */
    public void insert(V value, K id, int priority) {
        Node<V> newNode = new Node<>(value, priority); 
        for (Group<K, V> group : groups) {
            if (group.id.equals(id)) { // Is new value associated with this group?
                group.add(newNode);
                return;
            }
        }
        groups.add(new Group<>(newNode, id, groups)); // No association found, create a new group
    }

    /**
     * Remove and return the highest priority item from this queue
     * @return Group of items or null if empty
     */
    public Group<K, V> extract() {
        return groups.poll();
    }

    /**
     * Peek the highest priority group
     * @return Group of items or null if empty
     */
    public Group<K, V> peek() {
        return groups.peek();
    }
}

class Group<K, V> implements Comparator<Group<K, V>> {
    public K id; // Friend (or association) identification, e.g an integer
    public int priority; // Total priority of group
    private Queue<Node<V>> values; // Items in this group
    private Queue<Group<K, V>> groupQueue; // Keep a reference to group priority queue for updating

    public Queue<Node<V>> getValues() {
        return values;
    }

    public Group(Node<V> value, K id, Queue<Group<K, V>> groupQueue) {
        values = new PriorityQueue<>(new Node<>());
        values.add(value);
        priority = value.priority;
        this.id = id;
        this.groupQueue = groupQueue;
    }

    public Group() {} // When used as a comparator

    public void add(Node<V> newNode) {
        values.add(newNode);
        priority += newNode.priority;
        if (!groupQueue.isEmpty()) { // Trigger reheapify to keep the heap order
            groupQueue.add(groupQueue.poll()); 
        }
    }

    public int compare(Group<K, V> a, Group<K, V> b) {
        if (a.priority == b.priority) return 0;
        if (a.priority < b.priority) return 1;
        return -1;
    }
}

class Node<V> implements Comparator<Node<V>> {
    public int priority;
    public V value;

    public Node(V value, int priority) {
        this.value = value;
        this.priority = priority;
    }

    public Node() {} // When used as a comparator

    public int compare(Node<V> a, Node<V> b) {
        if (a.priority == b.priority) return 0;
        if (a.priority < b.priority) return 1;
        return -1;
    }
}