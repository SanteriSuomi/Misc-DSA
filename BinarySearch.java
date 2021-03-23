import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BinarySearch {
    public static void main(String[] args) {
        BinarySearch bs = new BinarySearch();
        List<Integer> l = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            l.add((int)(Math.random() * 100));
        }
        l.add(-4564);
        Collections.sort(l);
        System.out.println(bs.search(l, -4564));
        System.out.println(bs.search(l, 66));
    }

    public <T extends Comparable<T>> boolean search(List<T> items, T item) {
        if (items == null || items.isEmpty()) return false;
        return binarySearch(items, item, 0, items.size() - 1);
    }

    private <T extends Comparable<T>> boolean binarySearch(List<T> items, T item, int start, int end) {
        if (start >= end) return false;
        int mid = (start + end) / 2;
        int comp = items.get(mid).compareTo(item);
        if (comp == 0) return true;
        else if (comp > 0) {
            return binarySearch(items, item, start, mid);
        } else {
            return binarySearch(items, item, mid + 1, end);
        }
    }
}