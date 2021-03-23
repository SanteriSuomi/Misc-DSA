import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MoreThanHalf {
    public static void main(String[] args) {
        MoreThanHalf m = new MoreThanHalf();
        m.print(m.count(new int[]{5,5,5,5,5,3,3,3,2,5,8,8,8,8,8,8,8,8,8,8,8,8,8}));
    }

    public List<Integer> count(int[] arr) {
        List<Integer> e = new ArrayList<>();
        HashMap<Integer, Integer> h = new HashMap<>();
        int bound = arr.length / 2;
        for (int i = 0; i < arr.length; i++) {
            if (h.containsKey(arr[i]) && h.get(arr[i]) != -1) {
                int el = h.get(arr[i]);
                if (el + 1 > bound) {
                    e.add(arr[i]);
                    h.put(arr[i], -1);
                } else {
                    h.put(arr[i], h.get(arr[i]) + 1);
                }
            } else {
                h.put(arr[i], 1);
            }
        }
        return e;
    }

    public void print(List<Integer> e) {
        for (int i = 0; i < e.size(); i++) {
            System.out.print(e.get(i) + " ");
        }
    }
}