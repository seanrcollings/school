import java.util.HashMap;

/** Table for threads to enter their result into.
 * Internally stored as a HashMap. Usees the syncohinzed
 * keyword to insure the table is threadsafe
 */
class ResultTable<K, V> {
    private HashMap<K, V> table;

    public ResultTable() {
        table = new HashMap<K, V>();
    }

    public int size() {
        return table.size();
    }

    public synchronized void put(K key, V value) {
        table.put(key, value);
    }

    public V get(K key) {
        return table.get(key);
    }

}