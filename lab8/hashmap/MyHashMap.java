package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author OvidEros
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    private int size = 0;
    private Set<K> keySet = new HashSet<>();
    private double loadFactor = 0.75;
    private static final int INIT_SIZE = 16;


    /** Constructors */
    public MyHashMap() {
        buckets = createTable(INIT_SIZE);
    }

    public MyHashMap(int initialSize) {
        buckets = createTable(initialSize);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        buckets = createTable(initialSize);
        loadFactor = maxLoad;
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new LinkedList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
         Collection<Node>[] col = new Collection[tableSize];
         for (int i = 0; i < col.length; i++) {
             col[i] = createBucket();
         }
         return col;
    }

    @Override
    public void clear() {
        Arrays.fill(buckets, createBucket());
        size = 0;
    }

    @Override
    public boolean containsKey(K key) {
        Collection<Node> bucket= buckets[hashIndex(key)];
        for (Node node : bucket) {
            if (node.key.equals(key)) {
                return true;
            }
        }
        return false;
    }

    private int hashIndex(K key) {
        return Math.floorMod(key.hashCode(), buckets.length);
    }

    @Override
    public V get(K key) {
        Collection<Node> bucket= buckets[hashIndex(key)];
        for (Node node : bucket) {
            if (node.key.equals(key)) {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        Collection<Node> bucket= buckets[hashIndex(key)];
        keySet.add(key);
        boolean flag = false;
        for (Node node : bucket) {
            if (node.key.equals(key)) {
                node.value = value;
                flag = true;
            }
        }
        if (!flag) {
            Node newNode = createNode(key, value);
            bucket.add(newNode);
            size += 1;
        }
        checkResize();
    }

    @Override
    public Set<K> keySet() {
        return keySet;
    }

    private void checkResize() {
        if ((double) size / buckets.length >= loadFactor) {
            resize(buckets.length * 2);
        }
    }

    private void resize(int newSize) {
        Collection<Node>[] newBuckets = createTable(newSize);
        for (Collection<Node> bucket : buckets) {
            for (Node node : bucket) {
                newBuckets[hashIndex(node.key)].add(node);
            }
        }
        buckets = newBuckets;
    }

    @Override
    public V remove(K key) {
        Collection<Node> bucket= buckets[hashIndex(key)];
        for (Node node : bucket) {
            if (node.key.equals(key)) {
                bucket.remove(node);
                return node.value;
            }
        }
        return null;
    }

    @Override
    public V remove(K key, V value) {
        Collection<Node> bucket= buckets[hashIndex(key)];
        for (Node node : bucket) {
            if (node.key.equals(key) && node.value.equals(value)) {
                bucket.remove(node);
                return node.value;
            }
        }
        return null;
    }

    @Override
    public Iterator<K> iterator() {
        return keySet.iterator();
    }
}
