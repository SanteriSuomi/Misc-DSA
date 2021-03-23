import java.util.HashMap;
import java.util.Map;

/**
 * Implements a Least Recently Used cache data structure/algorithm.
 */
public class LRUCache {
    public static void main(String[] args) {
        LRUCache cache = new LRUCache(5);
        Page page = null;
        page = cache.access(10);
        page = cache.access(5);
        page = cache.access(10);
        page = cache.access(3);
        page = cache.access(10);
        page = cache.access(15);
        page = cache.access(18);
        page = cache.access(1);
        page = cache.access(12);
        page = cache.access(3);
    }

    private int maxSize; // Max amount of pages the cache can hold
    private int currentSize;
    private CacheList cache;
    private Map<Integer, Node> cacheMap;

    public LRUCache(int maxSize) {
        this.maxSize = maxSize;
        currentSize = 0;
        cache = new CacheList();
        cacheMap = new HashMap<>();
    }

    // Access a page in the cache
    public Page access(int pageNumber) {
        Node pageNode = cacheMap.get(pageNumber);
        if (pageNode != null) { // Page was already found in the cache, make it Most Recently Used and return it
            cache.moveToHead(pageNode);
            return pageNode.page;
        }
        if (currentSize >= maxSize) { // Evict least used (tail)
            evict();
        }
        return add(pageNumber); // Add new node with given page to the cache and return it
    }
    
    private void evict() {
        Page removedPage = cache.removeTail();
        cacheMap.remove(removedPage.number);
        currentSize--;
    }
    
    private Page add(int pageNumber) {
        Node newNode = new Node(new Page(pageNumber));
        cacheMap.put(pageNumber, newNode);
        cache.moveToHead(newNode);
        currentSize++;
        return newNode.page;
    }
}

class CacheList { // Doubly linked list with necessary operations
    public Node head; // Most recently used
    public Node tail; // Least recently used

    public void moveToHead(Node node) {
        if (head == null) {
            head = node;
            tail = node;
            return;
        }
        Node previousHead = head;
        head = node;
        head.next = previousHead;
        previousHead.previous = head;
    }

    public Page removeTail() {
        Node previousTail = tail;
        Node nextTail = tail.previous;
        tail.next = null;
        tail.previous = null;
        tail = nextTail;
        tail.next = null;
        return previousTail.page;
    }
}

class Node { // Cache node
    public Node next;
    public Node previous;
    public Page page;

    public Node(Page page) {
        this.page = page;
    }
}

class Page { // Cache page
    // public byte[] data; In a real situation there would be data in a page
    public int number;

    public Page(int number) {
        this.number = number;
    }
}