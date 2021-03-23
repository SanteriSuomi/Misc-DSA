/**
 * Simulate a simple autocomplete feature commonly seen in places such as mobile
 * keyboards and web browsers, using my own "Simple Trie".
 */
public class AutoComplete {
    public static void main(String[] args) {
        AutoComplete ac = new AutoComplete(new String[] { "abc", "abcd", "aa", "abbbaba", "abab" }, 2);
        // ac.input("ab"); // Imagine a user typing this on a e.g mobile keyboard. 
                        // The (naive) autocompleter gives every string that has a matching substring of the input at the start.
        
        // ac.input("a");
        ac.input("ab");
    }

    private int max; // Max number of search matches to display
    private SimpleTrie trie;

    public AutoComplete(String[] initial, int max) {
        trie = new SimpleTrie(initial);
        this.max = max;
    }

    public void input(String s) {
        for (String word : trie.insertAndGetMatches(s, max)) {
            System.out.println(word);
        }
    }
}