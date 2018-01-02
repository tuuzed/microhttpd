package tuuzed.lib.microhttpd.internal;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ArrayMap<K, V> implements Map<K, V> {
    private int size;
    private Object[] keys;
    private Object[] values;

    public ArrayMap() {
        this(1 << 4); // 16
    }

    public ArrayMap(int initialCapacity) {
        this.size = 0;
        this.keys = new Object[initialCapacity];
        this.values = new Object[initialCapacity];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(@NotNull Object key) {
        return indexOfKey(key) != -1;
    }

    @Override
    public boolean containsValue(@NotNull Object value) {
        return indexOfValue(value) != -1;
    }

    @Override
    public V get(@NotNull Object key) {
        int index = indexOfKey(key);
        return valueAt(index);
    }

    @Override
    public V put(@NotNull K key, @NotNull V value) {
        int index = indexOfKey(key);
        if (index == -1) {
            putVal(key, value);
        } else {
            values[index] = value;
        }
        return value;
    }

    private void putVal(K key, V value) {
        if (size == values.length) {
            resize();
        }
        keys[size] = key;
        values[size] = value;
        size++;
    }

    private void resize() {
        int newCapacity = size * 2;
        Object[] oldKeys = keys;
        Object[] oldValues = values;
        keys = new Object[newCapacity];
        values = new Object[newCapacity];
        System.arraycopy(oldKeys, 0, keys, 0, oldKeys.length);
        System.arraycopy(oldValues, 0, values, 0, oldValues.length);
    }

    @Override
    public V remove(@NotNull Object key) {
        int index = indexOfKey(key);
        if (index == -1) {
            return null;
        } else {
            Object value = values[index];
            int nextIndex = size - 1;
            for (int i = index; i < size; i++) {
                nextIndex = i + 1;
                keys[i] = keys[nextIndex];
                values[i] = values[nextIndex];
            }
            keys[nextIndex] = null;
            values[nextIndex] = null;
            size--;
            return (V) value;
        }
    }

    @Override
    public void putAll(@NotNull Map<? extends K, ? extends V> m) {
        for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
            K key = entry.getKey();
            if (key == null) continue;
            V value = entry.getValue();
            if (value == null) continue;
            put(key, value);
        }
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            keys[i] = null;
            values[i] = null;
        }
        size = 0;
    }

    @NotNull
    @Override
    public Set<K> keySet() {
        Set<K> list = new HashSet<>(size);
        for (int i = 0; i < size; i++) {
            list.add((K) keys[i]);
        }
        return list;
    }

    @NotNull
    @Override
    public Collection<V> values() {
        List<V> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add((V) values[i]);
        }
        return list;
    }

    @NotNull
    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> entries = new HashSet<>(size);
        for (int i = 0; i < size; i++) {
            final int finalI = i;
            entries.add(new Entry<K, V>() {
                @Override
                public K getKey() {
                    return (K) keys[finalI];
                }

                @Override
                public V getValue() {
                    return (V) values[finalI];
                }

                @Override
                public V setValue(@NotNull V value) {
                    values[finalI] = value;
                    return value;
                }

                @Override
                public String toString() {
                    return getKey() + "=" + getValue();
                }
            });
        }
        return entries;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        for (int i = 0; i < size; i++) {
            K key = keyAt(i);
            V value = valueAt(i);
            sb.append(key == this ? "(this Map)" : key);
            sb.append('=');
            sb.append(value == this ? "(this Map)" : value);
            if (i < size - 1) {
                sb.append(',').append(' ');
            }
        }
        return sb.append("}").toString();
    }

    public int indexOfKey(@NotNull Object key) {
        for (int i = 0; i < size(); i++) {
            if (key.equals(keys[i])) {
                return i;
            }
        }
        return -1;
    }

    public int indexOfValue(@NotNull Object value) {
        for (int i = 0; i < size(); i++) {
            if (value.equals(values[i])) {
                return i;
            }
        }
        return -1;
    }

    public K keyAt(int index) {
        if (index >= 0 && index < size) {
            return (K) keys[index];
        } else {
            return null;
        }
    }

    public V valueAt(int index) {
        if (index >= 0 && index < size) {
            return (V) values[index];
        } else {
            return null;
        }
    }
}
