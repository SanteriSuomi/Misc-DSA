import java.util.HashMap;
import java.util.Map;

/**
 * Each house i has a non-negative vi worth of value inside that you can steal.
 * However, due to the way the security systems of the houses are connected,
 * you’ll get caught if you rob two adjacent houses. What’s the maximum value
 * you can steal from the block?
 */
public class HouseRobberProblem {
    public static void main(String[] args) {
        HouseRobberProblem hrp = new HouseRobberProblem();
        long t = System.currentTimeMillis();
        System.out.println(hrp.compute(new int[] { 3, 10, 3, 1, 2, 6, 8, 3, 0, 4, 8, 2, 8, 5, 9, 5, 89, 45, 8, 5, 8, 3,
                2, 4, 34, 6, 6, 6, 0, 0, 0, 0, 4, 89, 45, 8, 4, 9, 100, 8, 5, 9, 0, 0, 0, 4, 7, 9, 4, 6, 89, 5, 3, 6, 7,
                3, 5, 7, 18 }, 0, 0, new HashMap<>())); // 12
        System.out.println(System.currentTimeMillis() - t);
    }

    public int compute(int[] v, int i, int n, Map<Integer, Integer> m) {
        if (i >= v.length)
            return n;
        if (m.containsKey(i))
            return m.get(i);
        int result = Math.max(compute(v, i + 1, n, m), compute(v, i + 2, n, m) + v[i]);
        m.put(i, result);
        return result;
    }
}