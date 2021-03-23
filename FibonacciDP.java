import java.util.HashMap;
import java.util.Map;

public class FibonacciDP {
    public static void main(String[] args) {
        FibonacciDP fdp = new FibonacciDP();
        System.out.println(fdp.fib1(10, new HashMap<>()));
        System.out.println(fdp.fib2(10));
    }

    /**
     * Simple recursive DP fib using a cache
     */
    public int fib1(int n, Map<Integer, Integer> c) {
        if (n == 0) return 0;
        if (n == 1) return 1;
        if (c.containsKey(n)) return c.get(n);
        int result = fib1(n - 1, c) + fib1(n - 2, c);
        c.put(n, result);
        return result;
    }

    /**
     * Simple iterative DP fib, bottom-up approach
     */
    public int fib2(int n) {
        int f1 = 1; // N - 1
        int f2 = 1; // N - 2
        int temp;
        for (int i = 2; i < n; i++) {
            temp = f1;
            f1 = f2;
            f2 = temp + f2;
        }
        return f2;
    }
}