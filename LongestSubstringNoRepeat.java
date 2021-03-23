import java.util.HashSet;
import java.util.Set;

public class LongestSubstringNoRepeat {
    public static void main(String[] args) {
        LongestSubstringNoRepeat lsnr = new LongestSubstringNoRepeat();
        System.out.println(lsnr.compute("pwwkew")); // 3 
        System.out.println(lsnr.compute("abcabcbb")); // 3
        System.out.println(lsnr.compute("bbbbb")); // 1
    }

    /**
     * Given a string, find the length of the longest substring without repeating characters.
     */
    public int compute(String s) {
        if (s == null) return 0;
        Set<Character> window = new HashSet<>();
        int highest = Integer.MIN_VALUE;
        for (int i = 0; i < s.length(); i++) {
            char curr = s.charAt(i);
            if (window.contains(curr)) {
                window.clear();
            } else {
                window.add(curr);
            }
            if (window.size() > highest) {
                highest = window.size();
            }
        }
        return highest;
    }
}