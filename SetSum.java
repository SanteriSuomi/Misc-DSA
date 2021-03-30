import java.util.HashSet;

public class SetSum {
    public static void main(String[] args) {
        SetSum ss = new SetSum();
        System.out.println(ss.setsum(new int[] { 8, 3, 6, 2, 7, 2, 5, 2 }, new int[] { 1, 7, 4, 2, 2, 3, 5, 1 }, 4));
        System.out.println(ss.setsum(new int[] { 4536,342,65423,645876,34234 }, new int[] { 4,6,3,1,7,8,3 }, 4));
        System.out.println(ss.setsum(new int[] { 4536,342,322938,645876,34234 }, new int[] { 4,6,3,322938,7,8,3 }, 645876));
    }

    public boolean setsum(int[] set1, int[] set2, int x) {
        HashSet<Integer> s1 = new HashSet<>();
        for (int i = 0; i < set1.length; i++) {
            s1.add(set1[i]);
        }
        for (int i = 0; i < set2.length; i++) {
            if (s1.contains(x - set2[i])) {
                return true;
            }
        }
        return false;
    }
}