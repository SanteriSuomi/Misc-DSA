import java.util.ArrayList;
import java.util.List;

/**
 * Heapsort
 * @author Santeri Suomi
 */
public class Heapsort {
    public static void main(String[] args) {
        List<Integer> l = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            l.add((int)(Math.random() * 100));
        }
        Heapsort hs = new Heapsort();
        hs.sort(l);
        System.out.println();
    }

    public <T extends Comparable<T>> void sort(List<T> items) {
        if (items == null || items.isEmpty()) return;
        for (int i = items.size() / 2 - 1; i >= 0; i--) { // Build a max heap from input
            maxHeapify(items, i, items.size());
        }
        for (int i = items.size() - 1; i > 0; i--) { // Heapsort body
            swap(items, 0, i); // Swap the highest element at root with last element
            maxHeapify(items, 0, i); // Heapify from root the restore max element to it, then reduce heap size (i) so largest element is unaffected (at the bottom)
        }
    }

    private <T extends Comparable<T>> void maxHeapify(List<T> items, int start, int size) {
        int max = start; 
        int left = getLeft(start);
        int right = getRight(start);
        if (left < size && items.get(left).compareTo(items.get(max)) > 0) {
            max = left; // Left child is greater
        }
        if (right < size && items.get(right).compareTo(items.get(max)) > 0) {
            max = right; // Right child is greater
        }
        if (max == start) return; // Both children are smaller, heap order is satisfied
        swap(items, max, start); // Swap the max child with it's original parent
        maxHeapify(items, max, size); // Restart process on the biggest child element index
    }

    private int getLeft(int parent) {
        return parent * 2 + 1;
    }

    private int getRight(int parent) {
        return parent * 2 + 2;
    }

    private <T extends Comparable<T>> void swap(List<T> items, int a, int b) {
        T temp = items.get(a);
        items.set(a, items.get(b));
        items.set(b, temp);
    }
}