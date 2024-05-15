import java.util.NoSuchElementException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.LinkedList;
import java.util.List;

public class HashtableMap<KeyType, ValueType> {

  // Instance field to store key-value pairs using chaining
  protected LinkedList<Pair>[] table = null;
  protected int size = 0;
  protected int capacity = 64;

  // Constructor with default capacity = 64
  public HashtableMap() {
    this(64);
  }

  // Constructor with specified capacity
  @SuppressWarnings("unchecked")
  public HashtableMap(int capacity) {
    // Initialize the table with the specified capacity
    this.capacity = capacity;
    table = (LinkedList<Pair>[]) new LinkedList<?>[capacity];
    // Initialize each bucket in the table with an empty LinkedList
    for (int i = 0; i < capacity; i++) {
      table[i] = new LinkedList<>();
    }
  }

  // Inner class to represent key-value pairs
  protected class Pair {
    public KeyType key;
    public ValueType value;

    public Pair(KeyType key, ValueType value) {
      this.key = key;
      this.value = value;
    }
  }

  /**
   * Adds a new key,value pair/mapping to this collection.
   * 
   * @param key   the key of the key,value pair
   * @param value the value that key maps to
   * @throws IllegalArgumentException if key already maps to a value
   * @throws NullPointerException     if key is null
   */
  @Override
  public void put(KeyType key, ValueType value) throws IllegalArgumentException {
    if (key == null) {
      throw new NullPointerException("Key cannot be null");
    }
    int index = Math.abs(key.hashCode()) % capacity;
    LinkedList<Pair> list = table[index];
    for (Pair pair : list) {
      if (pair.key.equals(key)) {
        throw new IllegalArgumentException("Key already exists");
      }
    }
    list.add(new Pair(key, value));
    size++;
    if ((double) size / capacity >= 0.8) {
      growAndRehash();
    }
  }

  /**
   * Checks whether a key maps to a value in this collection.
   * 
   * @param key the key to check
   * @return true if the key maps to a value, and false is the key doesn't map to a value
   */
  @Override
  public boolean containsKey(KeyType key) {
    if (key == null) {
      return false;
    }
    int index = Math.abs(key.hashCode()) % capacity;
    LinkedList<Pair> list = table[index];
    for (Pair pair : list) {
      if (pair.key.equals(key)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Retrieves the specific value that a key maps to.
   * 
   * @param key the key to look up
   * @return the value that key maps to
   * @throws NoSuchElementException when key is not stored in this collection
   */
  @Override
  public ValueType get(KeyType key) throws NoSuchElementException {
    if (key == null) {
      throw new NoSuchElementException("Key cannot be null");
    }
    int index = Math.abs(key.hashCode()) % capacity;
    LinkedList<Pair> list = table[index];
    for (Pair pair : list) {
      if (pair.key.equals(key)) {
        return pair.value;
      }
    }
    throw new NoSuchElementException("Key not found");
  }

  /**
   * Remove the mapping for a key from this collection.
   * 
   * @param key the key whose mapping to remove
   * @return the value that the removed key mapped to
   * @throws NoSuchElementException when key is not stored in this collection
   */
  @Override
  public ValueType remove(KeyType key) throws NoSuchElementException {
    if (key == null) {
      throw new NoSuchElementException("Key cannot be null");
    }
    int index = Math.abs(key.hashCode()) % capacity;
    LinkedList<Pair> list = table[index];
    for (Pair pair : list) {
      if (pair.key.equals(key)) {
        list.remove(pair);
        size--;
        return pair.value;
      }
    }
    throw new NoSuchElementException("Key not found");
  }

  /**
   * Removes all key,value pairs from this collection.
   */
  @Override
  public void clear() {
    for (int i = 0; i < capacity; i++) {
      table[i].clear();
    }
    size = 0;
  }

  /**
   * Retrieves the number of keys stored in this collection.
   * 
   * @return the number of keys stored in this collection
   */
  @Override
  public int getSize() {
    return size;
  }

  /**
   * Retrieves this collection's capacity.
   * 
   * @return the size of te underlying array for this collection
   */
  @Override
  public int getCapacity() {
    return capacity;
  }

  /**
   * Helper method to increase the capacity of the hashtable and rehash the key-value pairs. It
   * doubles the capacity of the hashtable and redistributes the key-value pairs to new indices
   * based on the updated capacity.
   */
  @SuppressWarnings("unchecked")
  private void growAndRehash() {
    int newCapacity = capacity * 2;
    LinkedList<Pair>[] newTable = (LinkedList<Pair>[]) new LinkedList<?>[newCapacity];
    for (int i = 0; i < newCapacity; i++) {
      newTable[i] = new LinkedList<>();
    }
    for (int i = 0; i < capacity; i++) {
      LinkedList<Pair> list = table[i];
      for (Pair pair : list) {
        int newIndex = Math.abs(pair.key.hashCode()) % newCapacity;
        newTable[newIndex].add(pair);
      }
    }
    table = newTable;
    capacity = newCapacity;
  }

  /**
   * Test case to verify the functionality of the put and get methods. Ensures that the put method
   * correctly adds key-value pairs to the hashtable, and the get method retrieves the correct value
   * for a given key. Also, checks behavior when adding duplicate keys.
   */
  @Test
  public void testPutAndGet() {
    HashtableMap<String, Integer> hashtable = new HashtableMap<>();
    // Add key-value pairs
    hashtable.put("key1", 10);
    hashtable.put("key2", 20);
    hashtable.put("key3", 40);
    hashtable.put("key4", 40);

    // Retrieve values using keys
    Assertions.assertEquals(10, hashtable.get("key1").intValue());
    Assertions.assertEquals(20, hashtable.get("key2").intValue()); // Value overridden
    Assertions.assertEquals(40, hashtable.get("key3").intValue());
    Assertions.assertEquals(40, hashtable.get("key4").intValue()); // Value overridden
  }

  /**
   * Test case to verify the functionality of the containsKey method. Verifies that the containsKey
   * method correctly identifies whether a key is present in the hashtable or not. Also, checks
   * behavior when the hashtable is empty.
   */
  @Test
  public void testContainsKey() {
    HashtableMap<String, Integer> hashtable = new HashtableMap<>();
    // Add key-value pairs
    hashtable.put("key1", 10);
    hashtable.put("key2", 20);
    hashtable.put("key3", 30);

    // Check if keys are present
    Assertions.assertTrue(hashtable.containsKey("key1"));
    Assertions.assertTrue(hashtable.containsKey("key2"));
    Assertions.assertTrue(hashtable.containsKey("key3"));
    Assertions.assertFalse(hashtable.containsKey("key4"));

    // Check behavior when hashtable is empty
    HashtableMap<String, Integer> emptyHashtable = new HashtableMap<>();
    Assertions.assertFalse(emptyHashtable.containsKey("key1"));
  }

  /**
   * Test case to verify the functionality of the remove method. Ensures that the remove method
   * correctly removes a key-value pair from the hashtable. Also, checks behavior when removing a
   * non-existent key.
   */
  @Test
  public void testRemove() {
    HashtableMap<String, Integer> hashtable = new HashtableMap<>();
    // Add key-value pairs
    hashtable.put("key1", 10);
    hashtable.put("key2", 20);
    hashtable.put("key3", 30);

    // Remove a key-value pair
    hashtable.remove("key1");
    Assertions.assertFalse(hashtable.containsKey("key1"));
    Assertions.assertThrows(NoSuchElementException.class, () -> hashtable.get("key1"));

    // Try to remove a non-existent key
    Assertions.assertThrows(NoSuchElementException.class, () -> hashtable.remove("key4"));
  }

  /**
   * Test case to verify the functionality of the clear method. Verifies that the clear method
   * removes all key-value pairs from the hashtable. Also, checks behavior when clearing an empty
   * hashtable.
   */
  @Test
  public void testClear() {
    HashtableMap<String, Integer> hashtable = new HashtableMap<>();
    // Add key-value pairs
    hashtable.put("key1", 10);
    hashtable.put("key2", 20);
    hashtable.put("key3", 30);

    // Clear the hashtable
    hashtable.clear();
    Assertions.assertEquals(0, hashtable.getSize());

    // Check behavior when hashtable is empty
    HashtableMap<String, Integer> emptyHashtable = new HashtableMap<>();
    emptyHashtable.clear();
    Assertions.assertEquals(0, emptyHashtable.getSize());
  }

  /**
   * Test case to verify the functionality of the getSize method. Ensures that the getSize method
   * returns the correct number of key-value pairs in the hashtable. Also, checks behavior when the
   * hashtable is empty.
   */
  @Test
  public void testGetSize() {
    HashtableMap<String, Integer> hashtable = new HashtableMap<>();
    // Add key-value pairs
    hashtable.put("key1", 10);
    hashtable.put("key2", 20);
    hashtable.put("key3", 30);

    // Check the size of the hashtable
    Assertions.assertEquals(3, hashtable.getSize());

    // Check behavior when hashtable is empty
    HashtableMap<String, Integer> emptyHashtable = new HashtableMap<>();
    Assertions.assertEquals(0, emptyHashtable.getSize());
  }


}
