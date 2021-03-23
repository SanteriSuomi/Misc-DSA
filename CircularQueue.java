import java.util.ArrayList;
import java.util.List;

/**
 * Circular Queue is a queue data structure that "wraps" back around circularly when it gets full, thus never getting "full".
 */
class CircularQueue<T> {
    public static void main(String[] args) {
        CircularQueue<Integer> cq = new CircularQueue<>(2);
        cq.enqueue(10);
        cq.enqueue(12);
        cq.enqueue(5);
        System.out.println(cq.dequeue());
        cq.enqueue(56); 
        cq.enqueue(26);
        System.out.println(cq.dequeue());
        System.out.println(cq.dequeue());
    }

    private List<T> values;
    private int maxSize;
    private int currSize;

    private int head;
    private int tail;

    public CircularQueue(int capacity) {
        maxSize = capacity;
        currSize = 0;
        head = -1;
        tail = -1;
        values = new ArrayList<>(10);
        for (int i = 0; i < capacity; i++) {
            values.add(null);
        }
    }

    public void enqueue(T item) {
        if (currSize >= maxSize) {
            System.err.println("Queue is full!");
            return;
        }
        if (head == -1) head = 0;
        tail = (tail + 1) % maxSize; // Keep tail index within min and max size
        values.set(tail, item);
        updateSize();
    }

    public T dequeue() {
        if (currSize == 0) {
            System.err.println("Queue is full!");
            return null;
        }
        T ret = values.get(head);
        if (head == tail) { // Reset queue
            head = -1;
            tail = -1;
        } else head = (head + 1) % maxSize; // Keep head index within min and max size
        updateSize();
        return ret;
    }

    private void updateSize() {
        if (tail >= head) {
            currSize = (tail - head) + 1;
        } else {
            currSize = (maxSize - (head - tail)) + 1;
        }
    }
}