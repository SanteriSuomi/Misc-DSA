import java.util.ArrayDeque;
import java.util.Deque;

public class BalancedParantheses {
    public static void main(String[] args) {
        BalancedParantheses bp = new BalancedParantheses();
        System.out.println(bp.balanced("((())())()"));
        System.out.println(bp.balanced(")()("));
        System.out.println(bp.balanced("())"));
    }

    public boolean balanced(String s) {
        if (s == null || s.isEmpty()) return false;
        Deque<Character> dq = new ArrayDeque<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == ')') {
                if (!dq.isEmpty() && dq.peekLast() == '(') {
                    dq.pollLast();
                } else {
                    dq.addLast(c);
                }
            } else {
                dq.addLast(c);
            }
        }
        return dq.isEmpty();
    }
}