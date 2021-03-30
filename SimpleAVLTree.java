/**
 * Self-balancing binary search tree (AVL tree).
 * @author Santeri Suomi
 * Huge thanks (and most credits) to https://www.baeldung.com/java-avl-trees and some other sites I might have unfortunately forgot the name of.
 */
public class SimpleAVLTree<T extends Comparable<T>> {
    public static void main(String[] args) {
        SimpleAVLTree<Integer> sat = new SimpleAVLTree<>();
        sat.insert(50);
        sat.insert(49);
        sat.insert(51);
        sat.remove(50);
    }

    private Node<T> root;

    public boolean search(T data) {
        if (root == null) return false;
        Node<T> current = root;
        while (current != null) {
            int res = current.data.compareTo(data);
            if (res == 0) {
                return true;
            }
            if (res < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return false;
    }

    public void insert(T data) {
        if (root == null) {
            root = new Node<>(data);
            return;
        }
        try {
            ins(root, data);
        } catch (DuplicateItemException e) {
            System.out.println(e);
        }
    }

    private Node<T> ins(Node<T> root, T data) throws DuplicateItemException {
        if (root == null) {
            return new Node<>(data);
        } else if (root.data.compareTo(data) > 0) {
            root.left = ins(root.left, data);
        } else if (root.data.compareTo(data) < 0) {
            root.right = ins(root.right, data);
        } else {
            throw new DuplicateItemException("No duplicates allowed!");
        }
        return rebalance(root); // Recursion reached the bottom of tree (call stack), start unwinding from bottom to top while rebalancing nodes 
    }

    public void remove(T data) {
        if (root == null) return;
        rem(root, data);
    }

    private Node<T> rem(Node<T> root, T data) {
        if (root == null) {
            return root;
        } else if (root.data.compareTo(data) > 0) {
            root.left = rem(root.left, data);
        } else if (root.data.compareTo(data) < 0) {
            root.right = rem(root.right, data);
        } else {
            if (root.left == null || root.right == null) {
                if (root.left == null) root = root.right;
                else root = root.left;
            } else {
                Node<T> mostLeft = mostLeftChild(root.right);
                root.data = mostLeft.data; // Switch root and most left child of right child
                root.right = rem(root.right, root.data); // Delete old (most left) as the data has been replaced
            }
        }
        if (root != null) {
            return rebalance(root); // Recursion reached the bottom of tree (call stack), start unwinding from bottom to top while rebalancing nodes 
        }
        return root;
    }

    private Node<T> mostLeftChild(Node<T> root) { // Get the most left child of a node
        while (root.left != null) {
            root = root.left;
        }
        return root;
    }

    private Node<T> rebalance(Node<T> node) {
        updateHeight(node);
        int balance = getBalanceFactor(node);
        if (balance >= 2) { // Left heavy (need to rotate right)
            if (getHeight(node.left.left) > getHeight(node.left.right)) {
                node = rotateRight(node);
            } else {
                node.left = rotateLeft(node.left);
                node = rotateRight(node);
            }
        } else if (balance <= -2) { // Right heavy (need to rotate left)
            if (getHeight(node.right.right) > getHeight(node.right.left)) {
                node = rotateLeft(node);
            } else {
                node.right = rotateRight(node.right);
                node = rotateLeft(node);
            }
        }
        return node;
    }

    private Node<T> rotateRight(Node<T> node) { // Perform right rotation
        Node<T> left = node.left;
        Node<T> leftRight = left.right;
        left.right = node;
        node.left = leftRight;
        updateHeight(node);
        updateHeight(left);
        return left;
    }

    private Node<T> rotateLeft(Node<T> node) { // Perform left rotation
        Node<T> right = node.right;
        Node<T> rightRight = right.left;
        right.left = node;
        node.right = rightRight;
        updateHeight(node);
        updateHeight(right);
        return right;
    }

    private void updateHeight(Node<T> node) { // Update height of node
        node.height = 1 + Math.max(getHeight(node.right), getHeight(node.left));
    }

    private int getHeight(Node<T> node) { // Get the height of node (-1 if null)
        if (node == null) return -1;
        return node.height;
    }

    private int getBalanceFactor(Node<T> node) { // Get the balance at of any given node (left subtree height - right subtree height)
        if (node == null) return 0;
        return getHeight(node.left) - getHeight(node.right);
    }
}

class Node<T> {
    public T data;
    public Node<T> left, right;
    public int height;

    public Node(T data) {
        this.data = data;
    }
}

class DuplicateItemException extends Exception {
    private static final long serialVersionUID = 1L;
    public DuplicateItemException(String msg) {
        super(msg);
    }
}