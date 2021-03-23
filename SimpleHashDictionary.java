import java.util.ArrayList;
import java.util.List;

/**
 * Hash dictionary using linked chaining for collision avoidance.
 * @author Santeri Suomi
 */
public class SimpleHashDictionary<K, V> {
    public static void main(String[] args) {
        SimpleHashDictionary<Integer, String> sd = new SimpleHashDictionary<>();
        sd.insert(10, "value1");
        sd.insert(17, "value2");
        System.out.println(sd.search(17));
        sd.delete(10);
        System.out.println(sd.search(10));
        sd.insert(10, "value3");
        System.out.println(sd.search(10));
        sd.insert(15, "value2");
        sd.insert(5, "value3");
        sd.insert(7, "value45");
        sd.insert(2, "value46");
        sd.insert(10, "value50");
        sd.insert(60, "value90");
        sd.insert(9870, "value41");
        System.out.println(sd.search(1));
        System.out.println(sd.search(2));
        sd.insert(2, "value49");
        System.out.println(sd.search(2));
        sd.insert(2, "value100000");
        System.out.println(sd.search(2));
    }

    private int capacity;
    private List<Node<K, V>> values;
    private static final int HASH_VALUE = 7;

    public SimpleHashDictionary() {
        initializeList(5);
    }

    private void initializeList(int size) {
        values = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            values.add(new Node<>(null, null));
        }
        capacity = size;
    }

    public void insert(K key, V value) {
        int i = hash(key);
        if (i >= capacity) {
            increaseCapacity();
        }
        Node<K, V> head = values.get(i);
        if (head.value == null || head.key == key) {
            head.key = key;
            head.value = value;
            return;
        }
        traverseAndInsert(head, key, value);
    }

    public void delete(K key) {
        int i = hash(key);
        if (i < capacity) {
            Node<K, V> curr = values.get(i);
            if (curr.key == key) {
                curr.key = null;
                curr.value = null;
                return;
            }
            traverseAndRemove(curr, key);
        }
    }

    public V search(K key) {
        int i = hash(key);
        if (i < capacity) {
            Node<K, V> head = values.get(i);
            if (head.key == key) {
                return head.value;
            }
            return traverseAndGetValue(head, key);
        }
        return null;
    }

    private void increaseCapacity() {
        int newCapacity = (int)(capacity * 1.5);
        List<Node<K, V>> newL = new ArrayList<>(newCapacity);
        capacity = newCapacity;
        addAll(values, newL);
        values = newL;
    }

    private void addAll(List<Node<K, V>> oldL, List<Node<K, V>> newL) {
        for (int i = 0; i < capacity; i++) {
            if (i < oldL.size()) {
                newL.add(oldL.get(i));
                continue;
            }
            newL.add(new Node<>());
        }
    }

    private void traverseAndInsert(Node<K, V> curr, K key, V value) {
        Node<K, V> newNode = new Node<>(key, value);
        Node<K, V> prev;
        while (curr.next != null) {
            prev = curr;
            curr = curr.next;
            if (curr.key.equals(key)) {
                prev.next = newNode;
                newNode.next = curr;
                return;
            }
        }
        curr.next = newNode;
    }

    private void traverseAndRemove(Node<K, V> curr, K key) {
        Node<K, V> prev = null;
        while (curr.next != null && !curr.key.equals(key)) {
            prev = curr;
            curr = curr.next;
        }
        if (prev != null) {
            prev.next = curr.next;
        }
        curr.next = null;
    }

    private V traverseAndGetValue(Node<K, V> curr, K key) {
        while (curr.next != null) {
            curr = curr.next;
            if (curr.key.equals(key)) {
                return curr.value;
            }
        }
        return null;
    }

    private int hash(K key) {
        return key.hashCode() % HASH_VALUE;
    }
}

class Node<K, V> {
    public K key = null;
    public V value = null;
    public Node<K, V> next;

    public Node() { }

    public Node(K key, V value) {
        this.key = key;
        this.value = value;
    }
}