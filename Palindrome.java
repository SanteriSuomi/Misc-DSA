public class Palindrome {
    public static void main(String[] args) {
        Palindrome p = new Palindrome();
        System.out.println(p.isPalindrome("abba"));
        System.out.println(p.isPalindrome("Lesen, Esel"));
        System.out.println(p.isPalindrome("aippuakauppias'"));
        System.out.println(p.isPalindrome("Géza kék az ég"));
        System.out.println(p.isPalindrome("Aaro saattoi ottaa soraa"));
        System.out.println(p.isPalindrome(""));
        System.out.println(p.isPalindrome("Roma tenet amor"));
    }

    private boolean isPalindrome(String s) {
        if (s == null) return false;
        int length = s.length() - 1;
        int start = 0;
        int mid = length / 2;
        int end = length;
        char[] startChar = new char[1]; // Array with one element to pass-by-reference
        char[] endChar = new char[1];
        while(end >= mid && start <= mid) { // When pointer(s) are mid-way we have checked every possible character
            getLowerCase(s, start, startChar); // Convert char to lowercase
            start = offset(s, startChar, start, true); // If spaces are detected, offset pointer by one for each that is found
            getLowerCase(s, end, endChar);
            end = offset(s, endChar, end, false);
            if (startChar[0] != endChar[0]) return false;
            start++;
            end--;
        }
        return true;
    }

    private void getLowerCase(String s, int i, char[] c) {
        c[0] = Character.toLowerCase(s.charAt(i));
    }

    private int offset(String s, char[] c, int i, boolean add) {
        while (c[0] == ' ' /*|| c[0] == ','*/) { // Could potentially ignore any character such as commas
            i = add ? ++i : --i; // Increment or decrement based on boolean passed to the method
            getLowerCase(s, i, c);
        }
        return i;
    }
}  