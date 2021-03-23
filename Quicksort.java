import java.util.ArrayList;
import java.util.List;

public class Quicksort {
    public static void main(String[] args) {
        Quicksort qs = new Quicksort();
        List<Integer> al = createData();
        qs.qsort(al);
        printData(al);
    }

    private static List<Integer> createData() {
        List<Integer> al = new ArrayList<>();
        for (int i = 0; i < 500; i++) {
            al.add((int) (Math.random() * 100));
        }
        return al;
    }

    private static void printData(List<Integer> al) {
        for (int i = 0; i < al.size(); i++) {
            System.out.print(al.get(i) + " ");
        }
    }

    public <T extends Comparable<T>> void qsort(List<T> items) {
        sort(items, 0, items.size() - 1);
    }

    private <T extends Comparable<T>> void sort(List<T> items, int low, int high) {
        if (low < high) {
            int pi = partition(items, low, high);
            sort(items, low, pi - 1);
            sort(items, pi + 1, high);
        }
    }

    private <T extends Comparable<T>> Integer partition(List<T> items, int low, int high) {
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            if (items.get(j).compareTo(items.get(high)) < 0) {
                i++;
                swap(items, i, j);
            }
        }
        swap(items, i + 1, high);
        return i + 1;
    }

    private <T extends Comparable<T>> void swap(List<T> items, int i, int j) {
        T temp = items.get(i);
        items.set(i, items.get(j));
        items.set(j, temp);
    }
}