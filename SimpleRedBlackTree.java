public class SimpleRedBlackTree<K extends Comparable<K>, V> {
    public static void main(String[] args) {
        SimpleRedBlackTree<Integer, Integer> srbt = new SimpleRedBlackTree<>();
        srbt.put(1, 1);
        srbt.put(2, 5);
        srbt.put(4, 10);
        System.out.println(srbt.get(4));
        srbt.put(5, 3);
        System.out.println(srbt.get(5));
        System.out.println(srbt.contains(2));
        System.out.println(srbt.isRedBlackTree());
    }

    private Node root;
    private Node cache;

    public void put(K key, V val) {
        root = insert(root, key, val);
        root.color = Color.BLACK;
    }

    private Node insert(Node h, K key, V val) {
        if (h == null) {
            cache = new Node(key, val, Color.RED, 1);
            return cache;
        }
        
        int cmp = key.compareTo(h.key);
        if (cmp < 0) h.left = insert(h.left, key, val);
        else if (cmp > 0) h.right = insert(h.right, key, val);
        else h.val = val;
        
        // After recursive calls we go up the three, rotating and flipping as needed to retain balance
        if (!isRed(h.left) && isRed(h.right)) h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right)) flipColors(h);

        h.size = 1 + size(h.left) + size(h.right);
        return h;
    }

    public V get(K key) {
        if (key == null) return null;
        if (isCached(key)) return cache.val;
        Node h = search(root, key);
        if (h != null) {
            cache = h;
            return h.val;
        }
        return null;
    }

    private boolean isCached(K key) {
        return cache != null && key == cache.key;
    }

    public boolean contains(K key) {
        Node h = search(root, key);
        if (h != null) {
            cache = h;
            return true;
        }
        return false;
    }

    public Node search(Node h, K key) {
        if (h == null) return null;
        int cmp = key.compareTo(h.key);
        if (cmp < 0) return search(h.left, key);
        else if (cmp > 0) return search(h.right, key);
        else return h;              
    }

    /**
     * Validate that this is a red black tree. Red black tree contains no node with two red children,
     *  there are no right leaning red nodes, and the amount of black nodes in both subtrees are the same.
     * @return True or false
     */
    public boolean isRedBlackTree() {
        return is23(root) && (isBalanced(root.left) == isBalanced(root.right));
    }

    /**
     * Check that any node doesn't have two red children and there are no right leaning nodes.
     * @param h Subtree
     * @return True if no two red children and no right leaning nodes.
     */
    private boolean is23(Node h) {
        if (h == null) return true;
        boolean rightRed = isRed(h.right);
        if ((isRed(h.left) && rightRed) || rightRed) return false; 
        return is23(h.left) && is23(h.right);
    }

    /**
     * Check that the amount of black nodes in both left and right subtrees are the same.
     * @param h Subtree
     * @return True if the amount of black nodes in left and right subtrees are the same.
     */
    private int isBalanced(Node h) {
        if (h == null) return 0;
        else if (h.color == Color.BLACK) return 1 + isBalanced(h.left) + isBalanced(h.right);
        else return isBalanced(h.left) + isBalanced(h.right);
    }

    private Node rotateLeft(Node h) {
        Node x = h.right; // Node x is the node to be rotated
        h.right = x.left;
        x.left = h;
        x.color = h.color;
        h.color = Color.RED;
        x.size = h.size;
        h.size = 1 + size(h.left) + size(h.right);
        return x;
    }

    private Node rotateRight(Node h) {
        Node x = h.left; // Node x is the node to be rotated
        h.left = x.right;
        x.right = h;
        x.color = h.color;
        h.color = Color.RED;
        x.size = h.size;
        h.size = 1 + size(h.left) + size(h.right);
        return x;
    }

    private void flipColors(Node h) {
        h.color = Color.RED;
        h.left.color = Color.BLACK;
        h.right.color = Color.BLACK;
    }

    private boolean isRed(Node h) {
        if (h == null) return false;
        return h.color == Color.RED;
    }

    private int size(Node h) {
        if (h == null) return 0;
        return h.size;
    }

    private class Node {
        public K key;
        public V val;
        public Node left, right;
        public Color color;
        public int size; // Number of nodes in the subtree

        public Node(K key, V val, Color color, int size) {
            this.key = key;
            this.val = val;
            this.color = color;
            this.size = size;
        }
    }

    private enum Color {
        RED, 
        BLACK
    }
}