package bstmap;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    private Node<K, V> root;
    private int size = 0;
    private Set<K> keySet = new HashSet<>();

    private static class Node<K, V> {
        K key;
        V value;
        Node<K, V> left;
        Node<K, V> right;

        Node(K key, V value, Node<K, V> left, Node<K, V> right) {
            this.key = key;
            this.value = value;
            this.left = left;
            this.right = right;
        }
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    public void printInOrder() {
        print(root);
    }

    private void print(Node<K, V> now) {
        if (now == null) {
            return;
        }
        print(now.left);
        System.out.println(now.key);
        print(now.right);
    }

    @Override
    public int size() {
        return size;
    }

    /** Helper method to recursively search key. */
    private Node<K, V> searchKey(Node<K, V> now, K key) {
        if (now == null) {
            return null;
        }
        if (key.equals(now.key)) {
            return now;
        }
        if (key.compareTo(now.key) < 0) {
            return searchKey(now.left, key);
        }
        return searchKey(now.right, key);
    }

    @Override
    public boolean containsKey(K key) {
        if (searchKey(root, key) == null) {
            return false;
        }
        return true;
    }

    @Override
    public V get(K key) {
        if (searchKey(root, key) == null) {
            return null;
        }
        return searchKey(root, key).value;
    }

    /** Helper method to insert item. */
    private Node<K, V> insert(Node<K, V> now, K key, V value) {
        if (now == null) {
            keySet.add(key);
            return new Node<>(key, value, null, null);
        }
        if (key.compareTo(now.key) < 0) {
            now.left = insert(now.left, key, value);
        } else if (key.compareTo(now.key) > 0) {
            now.right = insert(now.right, key, value);
        } else {
            now.value = value;
        }
        return now;
    }

    @Override
    public void put(K key, V value) {
        root = insert(root, key, value);
        size += 1;
    }

    @Override
    public Set<K> keySet() {
        return keySet;
    }

    @Override
    public V remove(K key) {
        Node<K, V> result = searchKey(root, key);
        if (result == null) {
            return null;
        }
        V val = searchKey(root, key).value;
        root = delete(root, key);
        size -= 1;
        return val;
    }

    private Node<K, V> delete(Node<K, V> now, K key) {
        if (now == null) {
            return null;
        }
        int cmp = key.compareTo(now.key);
        if (cmp < 0) {
            now.left = delete(now.left, key);
        } else if (cmp > 0) {
            now.right = delete(now.right, key);
        } else {
            switch (countChildren(now)) {
                case 0:
                    return null;
                case -1:
                    return now.left;
                case 1:
                    return now.right;
                case 2:
                    Node<K, V> other = findBiggestLeft(now.left);
                    now.key = other.key;
                    now.value = other.value;
                    now.left = deleteMax(now.left);
                default:
                    return null;
            }
        }
        return now;
    }

    private Node<K, V> deleteMax(Node<K, V> now) {
        if (now.right == null) {
            return now.left;
        }
        now.right = deleteMax(now.right);
        return now;
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        return keySet.iterator();
    }

    private Node<K, V> findBiggestLeft(Node<K, V> now) {
        if (now.right == null) {
            return now;
        }
        return findBiggestLeft(now.right);
    }

    /** Calculate how many children a node have. */
    private int countChildren(Node<K, V> now) {
        if (now.left == null && now.right == null) {
            return 0;
        } else if (now.left != null && now.right == null) {
            return -1;
        } else if (now.left == null && now.right != null) {
            return 1;
        }
        return 2;
    }
}
