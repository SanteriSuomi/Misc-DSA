import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Quick & dirty prefix notation parser
 */
class SimpleInfixParser {
    public static void main(String[] args) {
        SimpleInfixParser s = new SimpleInfixParser();
        System.out.println(s.solve("(- (+ 2 1) 4)")); // -1
    }

    public int solve(String s) {
        StringBuilder str = new StringBuilder();
        Deque<Character> operators = new ArrayDeque<>();
        Deque<Integer> operands = new ArrayDeque<>();
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == ')') {
                calculate(operators, operands);
                continue;
            }
            if (s.charAt(i) == '+' || (s.charAt(i) == '-' && !Character.isDigit(s.charAt(i + 1)))
             || s.charAt(i) == '/' || s.charAt(i) == '*') {
                operators.addLast(s.charAt(i));
            } else if (Character.isDigit(s.charAt(i))) {
                i = getNumber(s, str, operands, i);
                if (s.charAt(i) == ')') i--;
            }
        }
        return operands.removeLast();
    }

    private void calculate(Deque<Character> operators, Deque<Integer> operands) {
        char operator = operators.removeLast();
        int first = operands.removeLast();
        int second = operands.removeLast();
        switch (operator) {
            case '+':
                operands.addLast(second + first);
                break;
            case '-':
                operands.addLast(second - first);
                break;
            case '/':
                operands.addLast(second / first);
                break;
            case '*':
                operands.addLast(second * first);
                break;
            default:
                break;
        }
    }

    private int getNumber(String s, StringBuilder str, Deque<Integer> operands, int i) {
        str.append(s.charAt(i));
        int j = i + 1;
        for(; j < s.length(); j++) {
            if (!Character.isDigit(s.charAt(j))) break;
            str.append(s.charAt(j));
        }
        if (s.charAt(i - 1) == '-') {
            operands.addLast(-Integer.parseInt(str.toString()));
        } else {
            operands.addLast(Integer.parseInt(str.toString()));
        }
        str.setLength(0);
        return j;
    }
}