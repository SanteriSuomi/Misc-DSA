import java.util.Arrays;
import java.util.HashSet;

public class SetUnion {
    public static void main(String[] args) {
        SetUnion su = new SetUnion();
        System.out.println(Arrays.toString(su.union(new int[]{4,8,3,0,7,4,2}, new int[]{9,4,0,5,3,6,2})));
    }

    public int[] union(int[] set1, int[] set2) {
        HashSet<Integer> h = new HashSet<>();
        for (int i = 0; i < set1.length; i++) {
            h.add(set1[i]);
        }
        for (int i = 0; i < set2.length; i++) {
            h.add(set2[i]);
        }
        int[] res = new int[h.size()];
        int i = 0;
        for (int j : h) {
            res[i] = j;
            i++;
        }
        Arrays.sort(res);
        return res;
    }
}