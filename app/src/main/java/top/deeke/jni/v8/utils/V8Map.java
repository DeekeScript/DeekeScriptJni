package top.deeke.jni.v8.utils;

import androidx.annotation.NonNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import top.deeke.jni.v8.Releasable;
import top.deeke.jni.v8.V8Value;

public class V8Map<V> implements Map<V8Value, V>, Releasable {

    private final Map<V8Value, V>       map;
    private final Map<V8Value, V8Value> twinMap;

    /**
     * Creates a V8Map.
     */
    public V8Map() {
        map = new HashMap<>();
        twinMap = new HashMap<>();
    }

    /*
     * (non-Javadoc)
     * @see java.io.Closeable#close()
     */
    @Override
    public void close() {
        this.clear();
    }

    /*
     * (non-Javadoc)
     * @see java.util.Map#size()
     */
    @Override
    public int size() {
        return map.size();
    }

    /*
     * (non-Javadoc)
     * @see java.util.Map#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    /*
     * (non-Javadoc)
     * @see java.util.Map#containsKey(java.lang.Object)
     */
    @Override
    public boolean containsKey(final Object key) {
        return map.containsKey(key);
    }

    /*
     * (non-Javadoc)
     * @see java.util.Map#containsValue(java.lang.Object)
     */
    @Override
    public boolean containsValue(final Object value) {
        return map.containsValue(value);
    }

    /*
     * (non-Javadoc)
     * @see java.util.Map#get(java.lang.Object)
     */
    @Override
    public V get(final Object key) {
        return map.get(key);
    }

    /*
     * (non-Javadoc)
     * @see java.util.Map#put(java.lang.Object, java.lang.Object)
     */
    @Override
    public V put(final V8Value key, final V value) {
        this.remove(key);
        V8Value twin = key.twin();
        twinMap.put(twin, twin);
        return map.put(twin, value);
    }

    /*
     * (non-Javadoc)
     * @see java.util.Map#remove(java.lang.Object)
     */
    @Override
    public V remove(final Object key) {
        V result = map.remove(key);
        V8Value twin = twinMap.remove(key);
        if (twin != null) {
            twin.close();
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * @see java.util.Map#putAll(java.util.Map)
     */
    @Override
    public void putAll(final Map<? extends V8Value, ? extends V> m) {
        for (java.util.Map.Entry<? extends V8Value, ? extends V> entry : m.entrySet()) {
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
        for (V8Value V8Value : twinMap.keySet()) {
            V8Value.close();
        }
        twinMap.clear();
    }

    /*
     * (non-Javadoc)
     * @see java.util.Map#keySet()
     */
    @NonNull
    @Override
    public Set<V8Value> keySet() {
        return map.keySet();
    }

    /*
     * (non-Javadoc)
     * @see java.util.Map#values()
     */
    @NonNull
    @Override
    public Collection<V> values() {
        return map.values();
    }

    /*
     * (non-Javadoc)
     * @see java.util.Map#entrySet()
     */
    @NonNull
    @Override
    public Set<java.util.Map.Entry<V8Value, V>> entrySet() {
        return map.entrySet();
    }

}