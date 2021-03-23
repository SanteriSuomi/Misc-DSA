
/**
 * Simple run length encoding data compression algorithm.
 */
public class RunLengthEncoding {
    public static void main(String[] args) {
        RunLengthEncoding rle = new RunLengthEncoding();
        String original = "WWWWWWWWWWWWBWWWWWWWWWWWWBBBWWWWWWWWWWWWWWWWWWWWWWWWBWWWWWWWWWWWWWW"; 
        String encoded = rle.encode(original);
        String decoded = rle.decode(encoded);
        System.out.println(original.equals(decoded));
    }

    public String encode(String s) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (; i < s.length(); i++) {
            int amount = 0;
            char c = s.charAt(i);
            int j = i;
            for (; j < s.length() && s.charAt(j) == c; j++) {
                amount++;
            }
            i = j - 1;
            sb.append(amount + "" + c);
        }
        return sb.toString();
    }

    public String decode(String s) {
        StringBuilder complete = new StringBuilder();
        StringBuilder digit = new StringBuilder();
        int i = 0;
        while (i < s.length()) {
            int j = i;
            for (; Character.isDigit(s.charAt(j)); j++) {
                digit.append(s.charAt(j));
            }
            i = j + 1;
            int n = Integer.parseInt(digit.toString());
            digit.setLength(0);
            for (int k = 0; k < n; k++) {
                complete.append(s.charAt(i - 1));
            }
        }
        return complete.toString();
    }
}