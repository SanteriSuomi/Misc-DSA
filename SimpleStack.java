public class SimpleStack<T extends Comparable<T>> {
    public static void main(String[] args) {
        SimpleStack<Integer> ss = new SimpleStack<>();
        ss.push(123);
        System.out.println(ss.peek());
        ss.push(50);
        System.out.println(ss.peek());
        ss.push(67);
        ss.pop();
        System.out.println(ss.peek());
        ss.push(2);
        ss.push(1000);
        ss.push(3235);
        ss.push(1);
        ss.pop();
        ss.pop();
        ss.pop();
        ss.pop();
        ss.push(9429);
        System.out.println(ss.peek());
    }

    public int size;
    private Node<T> head;

    public void push(T value) {
        Node<T> newNode = new Node<>(value);
        size++;
        if (head == null) {
            head = newNode;
            return;
        }
        Node<T> lastHead = head;
        head = newNode;
        head.next = lastHead;
        lastHead.prev = head;
    }

    public T pop() {
        if (head == null) return null;
        T value = head.value;
        Node<T> headNext = head.next;
        size--;
        if (headNext == null) {
            head = null;
            return value;
        }
        head.prev = null;
        head.next = null;
        headNext.prev = null;
        head = headNext;
        return value;
    }

    public T peek() {
        if (head == null) return null;
        return head.value;
    }
}

class Node<T> {
    public T value;
    public Node<T> next, prev;

    public Node(T value) {
        this.value = value;
    }
}