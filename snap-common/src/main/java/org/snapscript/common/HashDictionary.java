package org.snapscript.common;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * HashDictionary stores key/value pairs in a single array, where alternate
 * slots are keys and values. This is nicer to CPU caches as consecutive memory
 * addresses are very cheap to access. Entry objects are not stored in the table
 * like in java.util.HashMap. Instead of trying to deal with collisions in the
 * main array using Entry objects, we put a special object in the key slot and
 * put a regular Object[] in the value slot. The array contains the key value
 * pairs in consecutive slots, just like the main array, but it's a linear list
 * with no hashing.
 * <p/>
 * The final result is a Map implementation that's leaner than java.util.HashMap
 * and faster than Trove's THashMap. The best of both approaches unified
 * together, and thus the name HashDictionary.
 */
public class HashDictionary<K,V> {

   protected static final Object NULL_KEY = new Object() {
      @Override
      public boolean equals(Object obj) {
         throw new RuntimeException("Possible corruption through unsynchronized concurrent modification.");
      }

      @Override
      public int hashCode() {
         throw new RuntimeException("Possible corruption through unsynchronized concurrent modification.");
      }

      @Override
      public String toString() {
         return "HashDictionary.NULL_KEY";
      }
   };

   protected static final Object CHAINED_KEY = new Object() {
      @Override
      public boolean equals(Object obj) {
         throw new RuntimeException("Possible corruption through unsynchronized concurrent modification.");
      }

      @Override
      public int hashCode() {
         throw new RuntimeException("Possible corruption through unsynchronized concurrent modification.");
      }

      @Override
      public String toString() {
         return "HashDictionary.CHAINED_KEY";
      }
   };

   protected static final float DEFAULT_LOAD_FACTOR = 0.75f;

   protected static final int DEFAULT_INITIAL_CAPACITY = 2;

   private static final long serialVersionUID = 1L;

   protected transient Object[] table;

   protected transient int occupied;

   protected float loadFactor = DEFAULT_LOAD_FACTOR;

   protected int maxSize;

   public HashDictionary() {
      this.allocate(DEFAULT_INITIAL_CAPACITY << 1);
   }

   public HashDictionary(int initialCapacity) {
      this(initialCapacity, DEFAULT_LOAD_FACTOR);
   }

   public HashDictionary(int initialCapacity, float loadFactor) {
      if (initialCapacity < 0) {
         throw new IllegalArgumentException("initial capacity cannot be less than 0");
      }
      this.loadFactor = loadFactor;
      this.init(this.fastCeil(initialCapacity / loadFactor));
   }

   private int fastCeil(float v) {
      int possibleResult = (int) v;
      if (v - possibleResult > 0.0F) {
         possibleResult++;
      }
      return possibleResult;
   }

   protected int init(int initialCapacity) {
      int capacity = 1;
      while (capacity < initialCapacity) {
         capacity <<= 1;
      }

      return this.allocate(capacity);
   }

   protected int allocate(int capacity) {
      this.allocateTable(capacity << 1); // the table size is twice the capacity
                                         // to handle both keys and values
      this.computeMaxSize(capacity);

      return capacity;
   }

   protected void allocateTable(int sizeToAllocate) {
      this.table = new Object[sizeToAllocate];
   }

   protected void computeMaxSize(int capacity) {
      // need at least one free slot for open addressing
      this.maxSize = Math.min(capacity - 1, (int) (capacity * this.loadFactor));
   }

   protected final int index(Object key) {
      // This function ensures that hashCodes that differ only by
      // constant multiples at each bit position have a bounded
      // number of collisions (approximately 8 at default load factor).
      int h = key == null ? 0 : key.hashCode();
      h ^= h >>> 20 ^ h >>> 12;
      h ^= h >>> 7 ^ h >>> 4;
      return (h & (this.table.length >> 1) - 1) << 1;
   }

   public void clear() {
      if (this.occupied == 0) {
         return;
      }
      this.occupied = 0;
      Object[] set = this.table;

      for (int i = set.length; i-- > 0;) {
         set[i] = null;
      }
   }

   public V put(K key, V value) {
      int index = this.index(key);
      Object cur = this.table[index];
      if (cur == null) {
         this.table[index] = toSentinelIfNull(key);
         this.table[index + 1] = value;
         if (++this.occupied > this.maxSize) {
            this.rehash(this.table.length);
         }
         return null;
      }
      if (cur != CHAINED_KEY && this.nonNullTableObjectEquals(cur, key)) {
         V result = (V) this.table[index + 1];
         this.table[index + 1] = value;
         return result;
      }
      return this.chainedPut(key, index, value);
   }

   private V chainedPut(K key, int index, V value) {
      if (this.table[index] == CHAINED_KEY) {
         Object[] chain = (Object[]) this.table[index + 1];
         for (int i = 0; i < chain.length; i += 2) {
            if (chain[i] == null) {
               chain[i] = toSentinelIfNull(key);
               chain[i + 1] = value;
               if (++this.occupied > this.maxSize) {
                  this.rehash(this.table.length);
               }
               return null;
            }
            if (this.nonNullTableObjectEquals(chain[i], key)) {
               V result = (V) chain[i + 1];
               chain[i + 1] = value;
               return result;
            }
         }
         Object[] newChain = new Object[chain.length + 4];
         System.arraycopy(chain, 0, newChain, 0, chain.length);
         this.table[index + 1] = newChain;
         newChain[chain.length] = toSentinelIfNull(key);
         newChain[chain.length + 1] = value;
         if (++this.occupied > this.maxSize) {
            this.rehash(this.table.length);
         }
         return null;
      }
      Object[] newChain = new Object[4];
      newChain[0] = this.table[index];
      newChain[1] = this.table[index + 1];
      newChain[2] = toSentinelIfNull(key);
      newChain[3] = value;
      this.table[index] = CHAINED_KEY;
      this.table[index + 1] = newChain;
      if (++this.occupied > this.maxSize) {
         this.rehash(this.table.length);
      }
      return null;
   }

   public V getIfAbsentPut(K key, V value) {
      int index = this.index(key);
      Object cur = this.table[index];

      if (cur == null) {
         this.table[index] = toSentinelIfNull(key);
         this.table[index + 1] = value;
         if (++this.occupied > this.maxSize) {
            this.rehash(this.table.length);
         }
         return value;
      }
      if (cur != CHAINED_KEY && this.nonNullTableObjectEquals(cur, key)) {
         return (V) this.table[index + 1];
      }
      return this.chainedGetIfAbsentPut(key, index, value);
   }

   private V chainedGetIfAbsentPut(K key, int index, V value) {
      V result = value;
      if (this.table[index] == CHAINED_KEY) {
         Object[] chain = (Object[]) this.table[index + 1];
         int i = 0;
         for (; i < chain.length; i += 2) {
            if (chain[i] == null) {
               chain[i] = toSentinelIfNull(key);
               chain[i + 1] = value;
               if (++this.occupied > this.maxSize) {
                  this.rehash(this.table.length);
               }
               break;
            }
            if (this.nonNullTableObjectEquals(chain[i], key)) {
               result = (V) chain[i + 1];
               break;
            }
         }
         if (i == chain.length) {
            Object[] newChain = new Object[chain.length + 4];
            System.arraycopy(chain, 0, newChain, 0, chain.length);
            newChain[i] = toSentinelIfNull(key);
            newChain[i + 1] = value;
            this.table[index + 1] = newChain;
            if (++this.occupied > this.maxSize) {
               this.rehash(this.table.length);
            }
         }
      } else {
         Object[] newChain = new Object[4];
         newChain[0] = this.table[index];
         newChain[1] = this.table[index + 1];
         newChain[2] = toSentinelIfNull(key);
         newChain[3] = value;
         this.table[index] = CHAINED_KEY;
         this.table[index + 1] = newChain;
         if (++this.occupied > this.maxSize) {
            this.rehash(this.table.length);
         }
      }
      return result;
   }

   public int getCollidingBuckets() {
      int count = 0;
      for (int i = 0; i < this.table.length; i += 2) {
         if (this.table[i] == CHAINED_KEY) {
            count++;
         }
      }
      return count;
   }

   /**
    * Returns the number of JVM words that is used by this map. A word is 4
    * bytes in a 32bit VM and 8 bytes in a 64bit VM. Each array has a 2 word
    * header, thus the formula is: words = (internal table length + 2) + sum
    * (for all chains (chain length + 2))
    *
    * @return the number of JVM words that is used by this map.
    */
   public int getMapMemoryUsedInWords() {
      int headerSize = 2;
      int sizeInWords = this.table.length + headerSize;
      for (int i = 0; i < this.table.length; i += 2) {
         if (this.table[i] == CHAINED_KEY) {
            sizeInWords += headerSize + ((Object[]) this.table[i + 1]).length;
         }
      }
      return sizeInWords;
   }

   protected void rehash(int newCapacity) {
      int oldLength = this.table.length;
      Object[] old = this.table;
      this.allocate(newCapacity);
      this.occupied = 0;

      for (int i = 0; i < oldLength; i += 2) {
         Object cur = old[i];
         if (cur == CHAINED_KEY) {
            Object[] chain = (Object[]) old[i + 1];
            for (int j = 0; j < chain.length; j += 2) {
               if (chain[j] != null) {
                  this.put(this.nonSentinel(chain[j]), (V) chain[j + 1]);
               }
            }
         } else if (cur != null) {
            this.put(this.nonSentinel(cur), (V) old[i + 1]);
         }
      }
   }

   public V get(Object key) {
      int index = this.index(key);
      Object cur = this.table[index];
      if (cur != null) {
         Object val = this.table[index + 1];
         if (cur == CHAINED_KEY) {
            return this.getFromChain((Object[]) val, (K) key);
         }
         if (this.nonNullTableObjectEquals(cur, (K) key)) {
            return (V) val;
         }
      }
      return null;
   }

   private V getFromChain(Object[] chain, K key) {
      for (int i = 0; i < chain.length; i += 2) {
         Object k = chain[i];
         if (k == null) {
            return null;
         }
         if (this.nonNullTableObjectEquals(k, key)) {
            return (V) chain[i + 1];
         }
      }
      return null;
   }

   public boolean containsKey(Object key) {
      int index = this.index(key);
      Object cur = this.table[index];
      if (cur == null) {
         return false;
      }
      if (cur != CHAINED_KEY && this.nonNullTableObjectEquals(cur, (K) key)) {
         return true;
      }
      return cur == CHAINED_KEY && this.chainContainsKey((Object[]) this.table[index + 1], (K) key);
   }

   private boolean chainContainsKey(Object[] chain, K key) {
      for (int i = 0; i < chain.length; i += 2) {
         Object k = chain[i];
         if (k == null) {
            return false;
         }
         if (this.nonNullTableObjectEquals(k, key)) {
            return true;
         }
      }
      return false;
   }

   public boolean containsValue(Object value) {
      for (int i = 0; i < this.table.length; i += 2) {
         if (this.table[i] == CHAINED_KEY) {
            if (this.chainedContainsValue((Object[]) this.table[i + 1], (V) value)) {
               return true;
            }
         } else if (this.table[i] != null) {
            if (HashDictionary.nullSafeEquals(value, this.table[i + 1])) {
               return true;
            }
         }
      }
      return false;
   }

   private boolean chainedContainsValue(Object[] chain, V value) {
      for (int i = 0; i < chain.length; i += 2) {
         if (chain[i] == null) {
            return false;
         }
         if (HashDictionary.nullSafeEquals(value, chain[i + 1])) {
            return true;
         }
      }
      return false;
   }
   private static boolean nullSafeEquals(Object value, Object other)
   {
       if (value == null)
       {
           if (other == null)
           {
               return true;
           }
       }
       else if (other == value || value.equals(other))
       {
           return true;
       }
       return false;
   }

   public V removeKey(K key) {
      return this.remove(key);
   }


   public int getBatchCount(int batchSize) {
      return Math.max(1, this.table.length / 2 / batchSize);
   }


   protected void copyMap(HashDictionary<K, V> HashDictionary) {
      for (int i = 0; i < HashDictionary.table.length; i += 2) {
         Object cur = HashDictionary.table[i];
         if (cur == CHAINED_KEY) {
            this.copyChain((Object[]) HashDictionary.table[i + 1]);
         } else if (cur != null) {
            this.put(this.nonSentinel(cur), (V) HashDictionary.table[i + 1]);
         }
      }
   }

   private void copyChain(Object[] chain) {
      for (int j = 0; j < chain.length; j += 2) {
         Object cur = chain[j];
         if (cur == null) {
            break;
         }
         this.put(this.nonSentinel(cur), (V) chain[j + 1]);
      }
   }

   public V remove(Object key) {
      int index = this.index(key);
      Object cur = this.table[index];
      if (cur != null) {
         Object val = this.table[index + 1];
         if (cur == CHAINED_KEY) {
            return this.removeFromChain((Object[]) val, (K) key, index);
         }
         if (this.nonNullTableObjectEquals(cur, (K) key)) {
            this.table[index] = null;
            this.table[index + 1] = null;
            this.occupied--;
            return (V) val;
         }
      }
      return null;
   }

   private V removeFromChain(Object[] chain, K key, int index) {
      for (int i = 0; i < chain.length; i += 2) {
         Object k = chain[i];
         if (k == null) {
            return null;
         }
         if (this.nonNullTableObjectEquals(k, key)) {
            V val = (V) chain[i + 1];
            this.overwriteWithLastElementFromChain(chain, index, i);
            return val;
         }
      }
      return null;
   }

   private void overwriteWithLastElementFromChain(Object[] chain, int index, int i) {
      int j = chain.length - 2;
      for (; j > i; j -= 2) {
         if (chain[j] != null) {
            chain[i] = chain[j];
            chain[i + 1] = chain[j + 1];
            break;
         }
      }
      chain[j] = null;
      chain[j + 1] = null;
      if (j == 0) {
         this.table[index] = null;
         this.table[index + 1] = null;
      }
      this.occupied--;
   }

   public int size() {
      return this.occupied;
   }

   public Set<K> keySet() {
      return new KeySet();
   }


   private int chainedHashCode(Object[] chain) {
      int hashCode = 0;
      for (int i = 0; i < chain.length; i += 2) {
         Object cur = chain[i];
         if (cur == null) {
            return hashCode;
         }
         Object value = chain[i + 1];
         hashCode += (cur == NULL_KEY ? 0 : cur.hashCode()) ^ (value == null ? 0 : value.hashCode());
      }
      return hashCode;
   }



   private K nonSentinel(Object key) {
      return key == NULL_KEY ? null : (K) key;
   }

   private static Object toSentinelIfNull(Object key) {
      if (key == null) {
         return NULL_KEY;
      }
      return key;
   }

   private boolean nonNullTableObjectEquals(Object cur, K key) {
      return cur == key || (cur == NULL_KEY ? key == null : cur.equals(key));
   }
   
   protected class KeySet implements Set<K>, Serializable {
   
       private static final long serialVersionUID = 1L;

       public boolean add(K key)
       {
           throw new UnsupportedOperationException("Cannot call add() on " + this.getClass().getSimpleName());
       }

       public boolean addAll(Collection<? extends K> collection)
       {
           throw new UnsupportedOperationException("Cannot call addAll() on " + this.getClass().getSimpleName());
       }

       public void clear()
       {
           HashDictionary.this.clear();
       }

       public boolean contains(Object o)
       {
           return HashDictionary.this.containsKey(o);
       }

       public boolean containsAll(Collection<?> collection)
       {
           for (Object aCollection : collection)
           {
               if (!HashDictionary.this.containsKey(aCollection))
               {
                   return false;
               }
           }
           return true;
       }

       public boolean isEmpty()
       {
           return false;
       }

       public Iterator<K> iterator()
       {
           return new KeySetIterator();
       }

       public boolean remove(Object key)
       {
           int oldSize = HashDictionary.this.occupied;
           HashDictionary.this.remove(key);
           return HashDictionary.this.occupied != oldSize;
       }

       public boolean removeAll(Collection<?> collection)
       {
           int oldSize = HashDictionary.this.occupied;
           for (Object object : collection)
           {
               HashDictionary.this.remove(object);
           }
           return oldSize != HashDictionary.this.occupied;
       }

       public void putIfFound(Object key, Map<K, V> other)
       {
           int index = HashDictionary.this.index(key);
           Object cur = HashDictionary.this.table[index];
           if (cur != null)
           {
               Object val = HashDictionary.this.table[index + 1];
               if (cur == CHAINED_KEY)
               {
                   this.putIfFoundFromChain((Object[]) val, (K) key, other);
                   return;
               }
               if (HashDictionary.this.nonNullTableObjectEquals(cur, (K) key))
               {
                   other.put(HashDictionary.this.nonSentinel(cur), (V) val);
               }
           }
       }

       private void putIfFoundFromChain(Object[] chain, K key, Map<K, V> other)
       {
           for (int i = 0; i < chain.length; i += 2)
           {
               Object k = chain[i];
               if (k == null)
               {
                   return;
               }
               if (HashDictionary.this.nonNullTableObjectEquals(k, key))
               {
                   other.put(HashDictionary.this.nonSentinel(k), (V) chain[i + 1]);
               }
           }
       }

       public boolean retainAll(Collection<?> collection)
       {

           return false;
       }

       public int size()
       {
           return HashDictionary.this.size();
       }

       public int getBatchCount(int batchSize)
       {
           return HashDictionary.this.getBatchCount(batchSize);
       }

       protected void copyKeys(Object[] result)
       {
           Object[] table = HashDictionary.this.table;
           int count = 0;
           for (int i = 0; i < table.length; i += 2)
           {
               Object x = table[i];
               if (x != null)
               {
                   if (x == CHAINED_KEY)
                   {
                       Object[] chain = (Object[]) table[i + 1];
                       for (int j = 0; j < chain.length; j += 2)
                       {
                           Object cur = chain[j];
                           if (cur == null)
                           {
                               break;
                           }
                           result[count++] = HashDictionary.this.nonSentinel(cur);
                       }
                   }
                   else
                   {
                       result[count++] = HashDictionary.this.nonSentinel(x);
                   }
               }
           }
       }

       @Override
       public boolean equals(Object obj)
       {
           if (obj instanceof Set)
           {
               Set<?> other = (Set<?>) obj;
               if (other.size() == this.size())
               {
                   return this.containsAll(other);
               }
           }
           return false;
       }

       @Override
       public int hashCode()
       {
           int hashCode = 0;
           Object[] table = HashDictionary.this.table;
           for (int i = 0; i < table.length; i += 2)
           {
               Object x = table[i];
               if (x != null)
               {
                   if (x == CHAINED_KEY)
                   {
                       Object[] chain = (Object[]) table[i + 1];
                       for (int j = 0; j < chain.length; j += 2)
                       {
                           Object cur = chain[j];
                           if (cur == null)
                           {
                               break;
                           }
                           hashCode += cur == NULL_KEY ? 0 : cur.hashCode();
                       }
                   }
                   else
                   {
                       hashCode += x == NULL_KEY ? 0 : x.hashCode();
                   }
               }
           }
           return hashCode;
       }

       public Object[] toArray()
       {
           int size = HashDictionary.this.size();
           Object[] result = new Object[size];
           this.copyKeys(result);
           return result;
       }

       public <T> T[] toArray(T[] result)
       {
           int size = HashDictionary.this.size();
           if (result.length < size)
           {
               result = (T[]) Array.newInstance(result.getClass().getComponentType(), size);
           }
           this.copyKeys(result);
           if (size < result.length)
           {
               result[size] = null;
           }
           return result;
       }


   }
   protected abstract class PositionalIterator<T> implements Iterator<T>
   {
       protected int count;
       protected int position;
       protected int chainPosition;
       protected boolean lastReturned;

       public boolean hasNext()
       {
           return this.count < HashDictionary.this.size();
       }

       public void remove()
       {
           if (!this.lastReturned)
           {
               throw new IllegalStateException("next() must be called as many times as remove()");
           }
           this.count--;
           HashDictionary.this.occupied--;

           if (this.chainPosition != 0)
           {
               this.removeFromChain();
               return;
           }

           int pos = this.position - 2;
           Object cur = HashDictionary.this.table[pos];
           if (cur == CHAINED_KEY)
           {
               this.removeLastFromChain((Object[]) HashDictionary.this.table[pos + 1], pos);
               return;
           }
           HashDictionary.this.table[pos] = null;
           HashDictionary.this.table[pos + 1] = null;
           this.position = pos;
           this.lastReturned = false;
       }

       protected void removeFromChain()
       {
           Object[] chain = (Object[]) HashDictionary.this.table[this.position + 1];
           int pos = this.chainPosition - 2;
           int replacePos = this.chainPosition;
           while (replacePos < chain.length - 2 && chain[replacePos + 2] != null)
           {
               replacePos += 2;
           }
           chain[pos] = chain[replacePos];
           chain[pos + 1] = chain[replacePos + 1];
           chain[replacePos] = null;
           chain[replacePos + 1] = null;
           this.chainPosition = pos;
           this.lastReturned = false;
       }

       protected void removeLastFromChain(Object[] chain, int tableIndex)
       {
           int pos = chain.length - 2;
           while (chain[pos] == null)
           {
               pos -= 2;
           }
           if (pos == 0)
           {
               HashDictionary.this.table[tableIndex] = null;
               HashDictionary.this.table[tableIndex + 1] = null;
           }
           else
           {
               chain[pos] = null;
               chain[pos + 1] = null;
           }
           this.lastReturned = false;
       }
   }

   protected class KeySetIterator extends PositionalIterator<K>
   {
       protected K nextFromChain()
       {
           Object[] chain = (Object[]) HashDictionary.this.table[this.position + 1];
           Object cur = chain[this.chainPosition];
           this.chainPosition += 2;
           if (this.chainPosition >= chain.length
                   || chain[this.chainPosition] == null)
           {
               this.chainPosition = 0;
               this.position += 2;
           }
           this.lastReturned = true;
           return HashDictionary.this.nonSentinel(cur);
       }

       public K next()
       {
           if (!this.hasNext())
           {
               throw new NoSuchElementException("next() called, but the iterator is exhausted");
           }
           this.count++;
           Object[] table = HashDictionary.this.table;
           if (this.chainPosition != 0)
           {
               return this.nextFromChain();
           }
           while (table[this.position] == null)
           {
               this.position += 2;
           }
           Object cur = table[this.position];
           if (cur == CHAINED_KEY)
           {
               return this.nextFromChain();
           }
           this.position += 2;
           this.lastReturned = true;
           return HashDictionary.this.nonSentinel(cur);
       }
   }


}
