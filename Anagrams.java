import java.util.HashSet;

public class Anagrams {
    public static void main(String[] args) {
        Anagrams a = new Anagrams();
        System.out.println(a.anagrams("silent", "listen"));
        System.out.println(a.anagrams("incest", "insect"));
        System.out.println(a.anagrams("silent", "lisen"));
    }
    
    public boolean anagrams(String s1, String s2) {
        if (s1 == null || s2 == null || s1.length() != s2.length()) {
            return false;
        }
        HashSet<Character> h = new HashSet<>();
        for (int i = 0; i < s1.length(); i++) {
            h.add(s1.charAt(i));
        }
        for (int i = 0; i < s2.length(); i++) {
            if (!h.contains(s2.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}