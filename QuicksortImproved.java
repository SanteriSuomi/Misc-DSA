import java.util.ArrayList;
import java.util.List;

public class QuicksortImproved {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 45; i++) {
            list.add((int)(Math.random() * 150) + 1);
        }
        QuicksortImproved qsi = new QuicksortImproved();
        long before = System.currentTimeMillis();
        qsi.sort(list);
        long after = System.currentTimeMillis();
        System.out.println(((after - before) / 1000));
        System.out.println();
    }

    public <T extends Comparable<T>> void sort(List<T> items) {
        quickSort(items, 0, items.size() - 1);
    }

    private <T extends Comparable<T>> void quickSort(List<T> items, int start, int end) {
        int length = end - start;
        if (length <= 1) return; // Consider sublist of length 1 or less "sorted"
        else if (length <= 10) {
            insertionSort(items, start, end); // Use insertion sort for smaller partitions
            return;
        }
        int partition = partition(items, start, end, medianOfThree(items, start, end));
        quickSort(items, start, partition - 1);
        quickSort(items, partition + 1, end);
    }

    private <T extends Comparable<T>> void insertionSort(List<T> items, int start, int end) {
        for (int i = start + 1; i <= end; i++) {
            T key = items.get(i); // Current element
            int j = i - 1; // Start from index one below the current key
            while (j >= 0 && items.get(j).compareTo(key) > 0) { // While we're inbounds of the array and the item is bigger than current key
                swap(items, j + 1, j); // Shift items up when item is bigger than key
                j--; // Go further down
            }
        }
    }

    private <T extends Comparable<T>> T medianOfThree(List<T> items, int start, int end) {
        int center = (start + end) / 2; // Center of the sublist
        if (items.get(start).compareTo(items.get(center)) > 0) { // Compare first and center
            swap(items, start, center);
        }
        if (items.get(start).compareTo(items.get(end)) > 0) { // Compare first and end
            swap(items, start, end);
        }
        if (items.get(center).compareTo(items.get(end)) > 0) { // Compare center and end
            swap(items, center, end);
        }
        swap(items, center, end - 1); // As pivot value is now at center, move pivot to end of partition
        return items.get(end - 1); // Return pivot
    }

    private <T extends Comparable<T>> int partition(List<T> items, int start, int end, T pivot) {
        end--; // Exclude pivot from partition
        int endPivot = end; // Store pivot position
        while (true) {
            while (items.get(++start).compareTo(pivot) < 0) { // Find biggest from left of the pivot, while incrementing pointer
            }
            while (items.get(--end).compareTo(pivot) > 0) { // Find smallest from right of the pivot, while decrementing pointer
            }
            if (start > end) break; // When start and end (left and right) pointers meet, this sublist is partitioned
            swap(items, start, end); // Else swap start and end to satisfy partitioning
        }
        swap(items, start, endPivot); // Restore pivot to the end of the sublist
        return start;
    }

    private <T extends Comparable<T>> void swap(List<T> items, int a, int b) {
        T temp = items.get(a);
        items.set(a, items.get(b));
        items.set(b, temp);
    }
}