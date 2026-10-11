public class Hashtable {

    private static class Entry {
        int key;
        Entry next;

        Entry(int key, Entry next) {
            this.key = key;
            this.next = next;
        }
    }

    private Entry[] buckets;
    private int size = 0;

    public HashTable() {
        buckets = new Entry[16];
    }
    private int indexOf(int key, int length) {
        int h = key * 31 + 7;
        if (h < 0) {
            h = -h;
        }
        return h % length;
    }

    public void insert(int key) {
        if (search(key)) {
            return;
        }
        if (size >= buckets.length * 3 / 4) {
            grow();
        }
        int i = indexOf(key, buckets.length);
        buckets[i] = new Entry(key, buckets[i]);
        size++;
    }

    public boolean search(int key) {
        Entry e = buckets[indexOf(key, buckets.length)];
        while (e != null) {
            if (e.key == key) {
                return true;
            }
            e = e.next;
        }
        return false;
    }

    public void delete(int key) {
        int i = indexOf(key, buckets.length);
        Entry prev = null;
        Entry e = buckets[i];
        while (e != null) {
            if (e.key == key) {
                if (prev == null) {
                    buckets[i] = e.next;
                } else {
                    prev.next = e.next;
                }
                size--;
                return;
            }
            prev = e;
            e = e.next;
        }
    }

    private void grow() {
        Entry[] old = buckets;
        buckets = new Entry[old.length * 2];
        for (int i = 0; i < old.length; i++) {
            Entry e = old[i];
            while (e != null) {
                Entry next = e.next;
                int j = indexOf(e.key, buckets.length);
                e.next = buckets[j];
                buckets[j] = e;
                e = next;
            }
        }
    }
}