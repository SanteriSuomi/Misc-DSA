import java.util.ArrayList;
import java.util.List;

/**
 * Mergesort
 * @author Santeri Suomi
 */
public class Mergesort {
    public static void main(String[] args) {
        List<Integer> items = getItems(500, 100);
        new Mergesort().sort(items);
        printItems(items);
    }

    private static List<Integer> getItems(int n, int m) {
        List<Integer> items = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            items.add((int)(Math.random() * m));
        }
        return items;
    }

    public static <T extends Comparable<T>> void printItems(List<T> items) {
        for (int i = 0; i < items.size(); i++) {
            System.out.print(items.get(i) + " ");
        }
    }

    public <T extends Comparable<T>> void sort(List<T> items) {
        if (items == null || items.isEmpty()) return;
        long before = System.currentTimeMillis(); // Store time before algorithms starts
        mergeSort(items, new ArrayList<>(items), 0, items.size() - 1);
        System.out.println((System.currentTimeMillis() - before) / 1000); // Algorithm running time in seconds
    }

    private <T extends Comparable<T>> void mergeSort(List<T> items, List<T> copy, int start, int end) {
        if (start >= end) return; // If current subarray is 1, stop dividing
        int mid = (start + end) / 2; // Get the middle index of current subarray
        mergeSort(items, copy, start, mid); // Split in to a new left subarray
        mergeSort(items, copy, mid + 1, end); // Split in to a new right subarray
        merge(items, copy, start, mid, end); // Merge both previously split subarrays in to a bigger array
    }

    private <T extends Comparable<T>> void merge(List<T> items, List<T> copy, int start, int mid, int end) {
        int itS = start; // Items (start) index
        int itM = mid + 1; // Items (mid) index
        int coI = 0; // Copy index
        while (itS <= mid && itM <= end) { // Copy elements from the left and right subarray onto the copy array
            if (items.get(itS).compareTo(items.get(itM)) < 0) {
                copy.set(coI++, items.get(itS++)); // Left subarray at item start is smaller
            } else {
                copy.set(coI++, items.get(itM++)); // Right subarray item at item middle is smaller
            }
        }
        while (itS <= mid) { // Copy any remaining elements from the left subarray
            copy.set(coI++, items.get(itS++)); 
        }
        while (itM <= end) { // copy any remaining elements from the right subarray
            copy.set(coI++, items.get(itM++));
        }
        for (int i = end ; i >= start; i--) { // Put the merged elements back in to the to be final items array
            items.set(i, copy.get(--coI));
        }

        // int leftInd = start; // Left array index
        // int rightInd = mid + 1; // Right array index
        // int mergedInd = 0; // Merged array index
        // boolean leftUsed = false; // Are all values of left array used?
        // boolean rightUsed = false; // Are all values of right array used?
        // while(mergedInd < end) {
        //     leftUsed = leftInd >= mid;
        //     rightUsed = rightInd >= end;
        //     if (leftUsed && rightUsed) {
        //         break;
        //     } else if (leftUsed) {
        //         copy.set(mergedInd++, items.get(rightInd++));
        //     } else if (rightUsed) {
        //         copy.set(mergedInd++, items.get(leftInd++));
        //     } else { // Neither are complete used
        //         if (items.get(leftInd).compareTo(items.get(rightInd)) < 0) {
        //             copy.set(mergedInd++, items.get(leftInd++));
        //         } else {
        //             copy.set(mergedInd++, items.get(rightInd++));
        //         }
        //     }
        // }
        // for (int i = end; i >= start; i--) {
        //     items.set(i, copy.get(--mergedInd));
        // }
    }
}