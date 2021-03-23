/**
 * Binary Search Tree implementation supporting insert, remove, contains, minimum & maximum.
 * @author Santeri Suomi
 * My first attempt on a binary search tree. In a hindsight, I probably should've used iteration more and the code is more complicated than it should be.
 */
public class SimpleBinarySearchTree<T extends Comparable<T>> {
    public static void main(String[] args) {
        SimpleBinarySearchTree<Integer> sb = new SimpleBinarySearchTree<>();
        sb.insert(20);
        sb.insert(50);
        sb.insert(25);
        sb.remove(20);
        sb.remove(50);
        sb.remove(25);
        sb.insert(977);
        sb.insert(22);
        sb.insert(88);
        System.out.println(sb.minimum());
        System.out.println(sb.maximum());
        sb.remove(977);
    }

    public int size;
    private Node<T> root;

    public void insert(T data) {
        size++;
        if (root == null) {
            root = new Node<>(data);
            return;
        }
        traverseAndInsert(root, new Node<>(data));
    }

    private T traverseAndInsert(Node<T> root, Node<T> node) {
        int result = node.data.compareTo(root.data);
        if (result > 0) {
            if (root.right == null) {
                node.parent = root;
                root.right = node;
                return null;
            }
            return traverseAndInsert(root.right, node);
        }
        if (result < 0) {
            if (root.left == null) {
                node.parent = root;
                root.left = node;
                return null;
            }
            return traverseAndInsert(root.left, node);
        }
        return null;
    }

    public void remove(T data) {
        FindResult<T> findResult = new FindResult<>();
        traverseAndFind(root, data, findResult); // Find node to be removed and store it in findresult object
        if (!findResult.found) return;
        size--;
        if (findResult.node.left == null && findResult.node.right == null) {
            noChildren(findResult.node);
        } else if (findResult.node.right != null && findResult.node.left == null) {
            oneChild(findResult, findResult.node.right);
        } else if (findResult.node.left != null && findResult.node.right == null) {
            oneChild(findResult, findResult.node.left);
        } else {
            ValueResult<T> valueResult = new ValueResult<>(findResult.node.left);
            traverseAndFindMaximum(findResult.node.left, valueResult);
            bothParents(findResult, valueResult); 
        }
    }

    private void noChildren(Node<T> node) {
        if (node.parent == null) {
            root = null;
            return;
        }
        if (node.parent.right == node) {
            node.parent.right = null;
        } else {
            node.parent.left = null;
        }
    }

    private void oneChild(FindResult<T> result, Node<T> child) {
        if (result.node == root) {
            root = child;
            root.parent = null;
            return;
        }
        if (result.node.parent.right == result.node) {
            result.node.parent.right = child;
        } else {
            result.node.parent.left = child;
        }
        child.parent = result.node.parent;
    }

    private void bothParents(FindResult<T> replace, ValueResult<T> with) {
        T replaceData = replace.node.data;
        replace.node.data = with.node.data;
        with.node.data = replaceData;
        noChildren(with.node);
    }

    public boolean contains(T data) {
        FindResult<T> result = new FindResult<>();
        traverseAndFind(root, data, result);
        return result.found;
    }

    private void traverseAndFind(Node<T> root, T data, FindResult<T> findResult) {
        if (root == null) return;
        if (root.data.equals(data)) {
            findResult.found = true;
            findResult.node = root;
            return;
        }
        int result = data.compareTo(root.data);
        if (result > 0) {
            traverseAndFind(root.right, data, findResult);
        } else if (result < 0) {
            traverseAndFind(root.left, data, findResult);
        }
    }

    public T minimum() {
        if (root == null) return null;
        return traverseAndFindMinimum(root, root.data);
    }

    private T traverseAndFindMinimum(Node<T> root, T currentMin) {
        if (root == null) return currentMin;
        if (currentMin.compareTo(root.data) > 0) {
            currentMin = root.data;
        }
        return traverseAndFindMinimum(root.left, currentMin);
    }

    public T maximum() {
        if (root == null) return null;
        return traverseAndFindMaximum(root, root.data);
    }

    private T traverseAndFindMaximum(Node<T> root, T currentMax) {
        if (root == null) return currentMax;
        if (currentMax.compareTo(root.data) < 0) {
            currentMax = root.data;
        }
        return traverseAndFindMaximum(root.right, currentMax);
    }

    private ValueResult<T> traverseAndFindMaximum(Node<T> root, ValueResult<T> result) {
        if (root == null) return result;
        if (result.value.compareTo(root.data) < 0) {
            result.value = root.data;
            result.node = root;
        }
        return traverseAndFindMaximum(root.right, result);
    }
}

class Node<T> {
    public T data;
    public Node<T> parent, left, right;

    public Node(T data) {
        this.data = data;
    }
}

class FindResult<T> {
    public boolean found;
    public Node<T> node;
}

class ValueResult<T> {
    public T value;
    public Node<T> node;

    public ValueResult(Node<T> initial) {
        this.node = initial;
        this.value = initial.data;
    }
}