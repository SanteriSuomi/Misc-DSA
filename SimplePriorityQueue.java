import java.util.ArrayList;
import java.util.List;

/**
 * Priority Queue implementation using a Binary (Min) Heap.
 * @author Santeri Suomi
 */
public class SimplePriorityQueue<T extends Comparable<T>> {
    public static void main(String[] args) {
        List<Integer> al = new ArrayList<>();
        for(int i = 0; i < 50; i++) {
            int n = (int)(Math.random() * 1000);
            if (Math.random() < 0.5) {
                al.add(-n);
            } else {
                al.add(n);
            }
        }
        SimplePriorityQueue<Integer> pq = new SimplePriorityQueue<>(al);
        while (pq.size() > 22) {
            pq.removeFirst();
        }
        System.out.println(pq.first());
    }

    private List<T> items;
    public int size() {
        return items.size();
    }
    public boolean isEmpty() {
        return items.isEmpty();
    }

    public SimplePriorityQueue() {
        items = new ArrayList<>(5);
    }

    public SimplePriorityQueue(List<T> items) {
        if (items != null && items.size() > 1) {
            for (int i = items.size() / 2; i >= 0; i--) {
                minHeapify(items, i);
            }
            this.items = items;
        } else {
            items = new ArrayList<>();
        }
    }

    private void minHeapify(List<T> items, int start) {
        int left = getLeft(start);
        int right = getRight(start);
        int min = start;
        if (left < items.size() && items.get(left).compareTo(items.get(min)) < 0) {
            min = left;
        }
        if (left < items.size() && items.get(right).compareTo(items.get(min)) < 0) {
            min = right;
        }
        if (min == start) return;
        swap(items, min, start);
        minHeapify(items, min);
    }

    public T first() {
        if (items.isEmpty()) return null;
        return items.get(0);
    }

    public T removeFirst() {
        if (items.isEmpty()) return null;
        T item = swapFirstAndLastAndRemove();
        siftDown(items, 0);
        return item;
    }

    private T swapFirstAndLastAndRemove() {
        int lastIndex = size() - 1;
        T item = items.get(0);
        swap(items, 0, lastIndex);
        items.remove(lastIndex);
        return item;
    }

    public void add(T item) {
        items.add(item);
        siftUp(items, size() - 1);
    }

    private void siftUp(List<T> items, int start) {
        T item = items.get(start);
        int current = start;
        int parent = getParent(current);
        while (parent >= 0 && current >= 0 && items.get(parent).compareTo(item) > 0) {
            swap(items, current, parent);
            current = parent;
            parent = getParent(current);
        }
    }

    public void remove(T item) {
        if (items.isEmpty()) return;
        int lastIndex = size() - 1;
        int currIndex = findItemIndex(item);
        if (currIndex != lastIndex && currIndex != -1) {
            swap(items, currIndex, lastIndex);
            items.remove(lastIndex);
            siftDown(items, currIndex);
        }
    }

    private int findItemIndex(T item) {
        int i = -1;
        for (int j = 0; j < size(); j++) {
            if (items.get(j).equals(item)) {
                i = j;
                break;
            }
        }
        return i;
    }

    private void siftDown(List<T> items, int start) {
        int parent = start;
        int left = getLeft(parent);
        int right = getRight(parent);
        while (right < items.size()) {
            if (items.get(left).compareTo(items.get(right)) < 0
                && items.get(parent).compareTo(items.get(left)) > 0) { // If left is small than right and parent is bigger than left
                swap(items, parent, left);
                parent = left;
            } else if (items.get(parent).compareTo(items.get(right)) > 0) { // Else right is bigger element, compare parent and right
                swap(items, parent, right);
                parent = right;
            } else { // Heap structure is satisfied
                break;
            }
            left = getLeft(parent);
            right = getRight(parent);
        }
    }

    private int getParent(int i) {
        return i / 2;
    }

    private int getLeft(int parent) {
        if (parent == 0) {
            return 1;
        }
        return parent * 2;
    }

    private int getRight(int parent) {
        if (parent == 0) {
            return 2;
        }
        return parent * 2 + 1;
    }

    private void swap(List<T> items, int a, int b) {
        T temp = items.get(a);
        items.set(a, items.get(b));
        items.set(b, temp);
    }
}