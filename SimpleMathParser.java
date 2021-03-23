import java.util.*;

/**
 * Simple mathematical operation parser. Convert infix to postfix and parses the result. 
 * Made for binarysearch.com problem "Calculator".
 */
class SimpleMathParser {
    public static void main(String[] args) {
        SimpleMathParser s = new SimpleMathParser();
        System.out.println(s.solve("1+2*4/6"));
        System.out.println(s.solve("-20-4-2+32-17+24-8/-31-22+7+27-20"));
        System.out.println(s.solve("0+14-12+9-25-15+7+25+13-12+12+12+30-1+19-29-29-12+4+4-17+18+4-2-3-20-7+0-21-10+14+28/-12"));
    }

    public int solve(String s) {
        return parse(infixToPostfix(s));
    }

    private int parse(String postfix) {
        StringBuilder sb = new StringBuilder();
        Deque<Integer> operands = new ArrayDeque<>();
        int i = 0;
        while (i < postfix.length()) {
            char c = postfix.charAt(i);
            if (Character.isDigit(c)) {
                i = addOperand(postfix, i, operands, sb);
            }
            else if (c != 'n' && c != ',') {
                operands.addLast(getCalculation(operands.removeLast(), c, operands.removeLast()));
            }
            i++;
        }
        return operands.peekLast();
    }

    private int addOperand(String s, int i, Deque<Integer> operands, StringBuilder sb) {
        boolean negative = i - 1 >= 0 && s.charAt(i - 1) == 'n';
        while (i < s.length() && Character.isDigit(s.charAt(i))) {
            sb.append(s.charAt(i));
            i++;
        }
        operands.addLast(negative ? -Integer.parseInt(sb.toString()) : Integer.parseInt(sb.toString()));
        sb.setLength(0);
        return i;
    }

    private int getCalculation(int operand1, char operator, int operand2) {
        switch(operator) {
            case '+':
                return operand2 + operand1;
            case '-':
                return operand2 - operand1;
            case '/':
                return (int)Math.floor(operand2 / (double)operand1);
            case '*':
                return operand2 * operand1;
            default: 
                throw new ArithmeticException("Operands or operator were invalid!");
        }
    }

    public String infixToPostfix(String s) {
        StringBuilder postfix = new StringBuilder();
        Deque<String> dq = new ArrayDeque<>();
        int i = 0;
        while (i < s.length()) {
            String c = Character.toString(s.charAt(i));
            if (Character.isDigit(c.charAt(0))) {
                i = addNumber(s, i, postfix);
            }
            else if (c.equals("(")) dq.addLast(c);
            else if (c.equals(")")) {
                while (!dq.isEmpty() && dq.peekLast().equals("(")) {
                    postfix.append(dq.removeLast());
                }
                dq.removeLast();
            } else if (i > 0 && !isOperator(s.charAt(i - 1))) {
                while(!dq.isEmpty() && getPrecedent(c) <= getPrecedent(dq.peekLast())) {
                    postfix.append(dq.removeLast());
                }
                dq.addLast(c);
            }
            i++;
        }
        while(!dq.isEmpty()) {
            postfix.append(dq.removeLast());
        }
        return postfix.toString();
    }

    private boolean isNegative(String s, int i) {
        return (i - 1 >= 0 && s.charAt(i - 1) == '-' && !Character.isDigit(i - 2 >= 0 ? s.charAt(i - 2) : ' ') 
            || (i - 2 >= 0 && isOperator(s.charAt(i - 2))));
    }

    private boolean isOperator(char c) {
        return c == '/' || c == '*' || c == '+' || c == '-';
    }

    private int addNumber(String s, int i, StringBuilder postfix) {
        if (isNegative(s, i)) postfix.append('n');
        while (i < s.length() && Character.isDigit(s.charAt(i))) {
            postfix.append(s.charAt(i));
            i++;
        }
        postfix.append(',');
        return i - 1;
    }
    
    private int getPrecedent(String operator) {
        switch(operator) {
            case "+":
            case "-":
                return 1;
            case "/":
            case "*":
                return 2;
            default:
                return -1;
        }
    }
}