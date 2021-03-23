import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class HuffmanCoding {
    public static void main(String[] args) {
        HuffmanCoding hf = new HuffmanCoding();
        String encoded = hf.encode("Hello, Huffman Coding!");
        System.out.println("Encoded: " + encoded);
        String decoded = hf.decode(encoded);
        System.out.println("Decoded: " + decoded);
    }

    private HuffmanNode root; // Root of current stored encoded tree

    public String decode(String code) {
        if (root == null) {
            System.err.println("You must encode a message first");
            return null;
        }
        StringBuilder decoded = new StringBuilder();
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < code.length(); i++) {
            char c = code.charAt(i);
            if (c == ' ') {
                String current = temp.toString();
                temp.setLength(0);
                decoded.append(getCharacter(current));
            } else {
                temp.append(c);
            }
        }
        decoded.append(getCharacter(temp.toString())); // Get last character
        return decoded.toString();
    }

    private char getCharacter(String code) {
        HuffmanNode current = root;
        for (int i = 0; i < code.length(); i++) {
            char c = code.charAt(i);
            if (c == '0') {
                current = current.left;
            } else {
                current = current.right;
            }
            if (current.left == null && current.right == null) {
                return current.character;
            }
        }
        return ' ';
    }

    public String encode(String input) {
        if (input == null || input.isEmpty()) return null;
        createTree(createQueue(createPairs(input)));
        Map<Character, String> codeTable = new HashMap<>();
        createCodeTable(root, "", codeTable);
        return createCodeFromTable(input, codeTable).toString();
    }

    public void createCodeTable(HuffmanNode root, String str, Map<Character, String> codeTable) {
        if (root == null)
            return;
        if (root.left == null && root.right == null) {
            codeTable.put(root.character, str);
            return;
        }
        createCodeTable(root.left, str + "0", codeTable);
        createCodeTable(root.right, str + "1", codeTable);
    }

    private StringBuilder createCodeFromTable(String input, Map<Character, String> codeTable) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            str.append(codeTable.get(c) + " ");
        }
        str.delete(str.length() - 1, str.length()); // Remove space from the end of string
        return str;
    }

    private Map<Character, Integer> createPairs(String input) {
        Map<Character, Integer> map = new HashMap<>();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (map.containsKey(c)) {
                map.put(c, map.get(c) + 1);
            } else {
                map.put(c, 1);
            }
        }
        return map;
    }

    private Queue<HuffmanNode> createQueue(Map<Character, Integer> map) {
        Queue<HuffmanNode> queue = new PriorityQueue<>(new HuffmanComparator());
        for (Map.Entry<Character, Integer> e : map.entrySet()) {
            queue.add(new HuffmanNode(e.getKey(), e.getValue()));
        }
        return queue;
    }

    private void createTree(Queue<HuffmanNode> queue) {
        while (queue.size() > 1) {
            HuffmanNode newNode = new HuffmanNode();
            newNode.left = queue.poll();
            newNode.right = queue.poll();
            newNode.frequency = newNode.left.frequency + newNode.right.frequency;
            root = newNode;
            queue.add(newNode);
        }
    }
}

class HuffmanNode implements Comparable<HuffmanNode> {
    public char character;
    public int frequency;
    public HuffmanNode left, right;

    public HuffmanNode() {
    }

    public HuffmanNode(char character, int frequency) {
        this.character = character;
        this.frequency = frequency;
    }

    public int compareTo(HuffmanNode hn) {
        if (this.frequency > hn.frequency)
            return 1;
        if (this.frequency < hn.frequency)
            return -1;
        return 0;
    }
}

class HuffmanComparator implements Comparator<HuffmanNode> {
    public int compare(HuffmanNode hn1, HuffmanNode hn2) {
        if (hn1.frequency > hn2.frequency)
            return 1;
        if (hn1.frequency < hn2.frequency)
            return -1;
        return 0;
    }
}