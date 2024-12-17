package top.deeke.jni.v8.utils;

import androidx.annotation.NonNull;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

/**
 * A custom map is needed because the existing HashMaps
 * do not allow self containment, and Hashtable do not
 * allow nulls as values.
 * <p>
 * This class is not considered API.
 */
class V8PropertyMap<V> implements Map<String, V> {

    private final Hashtable<String, V> map = new Hashtable<>();
    private final Set<String> nulls = new HashSet<>();

    /*
     * (non-Javadoc)
     * @see java.util.Map#size()
     */
    @Override
    public int size() {
        return map.size() + nulls.size();
    }

    /*
     * (non-Javadoc)
     * @see java.util.Map#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        return map.isEmpty() && nulls.isEmpty();
    }

    /*
     * (non-Javadoc)
     * @see java.util.Map#containsKey(java.lang.Object)
     */
    @Override
    public boolean containsKey(final Object key) {
        return map.containsKey(key) || nulls.contains(key);
    }

    /*
     * (non-Javadoc)
     * @see java.util.Map#containsValue(java.lang.Object)
     */
    @Override
    public boolean containsValue(final Object value) {
        if ((value == null) && !nulls.isEmpty()) {
            return true;
        } else if (value == null) {
            return false;
        }
        return map.containsValue(value);
    }

    /*
     * (non-Javadoc)
     * @see java.util.Map#get(java.lang.Object)
     */
    @Override
    public V get(final Object key) {
        if (nulls.contains(key)) {
            return null;
        }
        return map.get(key);
    }

    /*
     * (non-Javadoc)
     * @see java.util.Map#put(java.lang.Object, java.lang.Object)
     */
    @Override
    public V put(final String key, final V value) {
        if (value == null) {
            if (map.containsKey(key)) {
                map.remove(key);
            }
            nulls.add(key);
            return null;
        }
        if (nulls.contains(key)) {
            nulls.remove(key);
        }
        return map.put(key, value);
    }

    /*
     * (non-Javadoc)
     * @see java.util.Map#remove(java.lang.Object)
     */
    @Override
    public V remove(final Object key) {
        if (nulls.contains(key)) {
            nulls.remove(key);
            return null;
        }
        return map.remove(key);
    }

    /*
     * (non-Javadoc)
     * @see java.util.Map#putAll(java.util.Map)
     */
    @Override
    public void putAll(final Map<? extends String, ? extends V> m) {
        for (Entry<? extends String, ? extends V> entry : m.entrySet()) {
            this.put(entry.getKey(), entry.getValue());
        }
    }

    /*
     * (non-Javadoc)
     * @see java.util.Map#clear()
     */
    @Override
    public void clear() {
        map.clear();
        nulls.clear();
    }

    /*
     * (non-Javadoc)
     * @see java.util.Map#keySet()
     */
    @NonNull
    @Override
    public Set<String> keySet() {
        HashSet<String> result = new HashSet<String>(map.keySet());
        result.addAll(nulls);
        return result;
    }

    /*
     * (non-Javadoc)
     * @see java.util.Map#values()
     */
    @NonNull
    @Override
    public Collection<V> values() {
        ArrayList<V> result = new ArrayList<V>(map.values());
        for (int i = 0; i < nulls.size(); i++) {
            result.add(null);
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * @see java.util.Map#entrySet()
     */
    @NonNull
    @Override
    public Set<Entry<String, V>> entrySet() {
        HashSet<Entry<String, V>> result = new HashSet<Map.Entry<String, V>>(map.entrySet());
        for (String nullKey : nulls) {
            result.add(new AbstractMap.SimpleEntry<String, V>(nullKey, null));
        }
        return result;
    }

}