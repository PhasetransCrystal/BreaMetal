package com.phasetranscrystal.metal.helper;

import com.google.common.collect.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;

public final class ImmutableBiMultiMap<K, V> {
    private final ImmutableMultimap<K, V> forwardMap;
    private final ImmutableMap<V, K> reverseMap;
    private final ImmutableMap<K, V> mainResultMap;

    private ImmutableBiMultiMap(Builder<K, V> builder) {
        this.forwardMap = ImmutableMultimap.copyOf(builder.forwardMap);
        this.reverseMap = ImmutableMap.copyOf(builder.reverseMap);
        this.mainResultMap = ImmutableMap.copyOf(builder.mainResultMap);
    }

    // 获取正向映射的值集合
    public Collection<V> getValues(K key) {
        return forwardMap.get(key);
    }

    // 获取反向映射的键
    public K getKey(V value) {
        return reverseMap.get(value);
    }

    // 是否包含某个键
    public boolean containsKey(K key) {
        return forwardMap.containsKey(key);
    }

    // 是否包含某个值
    public boolean containsValue(V value) {
        return reverseMap.containsKey(value);
    }

    public V getMainResult(K key) {
        return mainResultMap.containsKey(key) ? mainResultMap.get(key) : forwardMap.get(key).stream().findAny().orElse(null);
    }

    public void foreach(BiConsumer<? super K, ? super V> action) {
        forwardMap.forEach(action);
    }

    public boolean isEmpty() {
        return forwardMap.isEmpty();
    }

    public static <K, V> Builder<K, V> builder() {
        return new Builder<>();
    }

    // 构建器
    public static class Builder<K, V> {
        private final Multimap<K, V> forwardMap = LinkedHashMultimap.create();
        private final HashMap<V, K> reverseMap = new HashMap<>();
        private final HashMap<K, V> mainResultMap = new HashMap<>();

        // 尝试添加键值对，若值已存在且对应不同键则返回 false
        public Builder<K, V> put(K key, V value) {
            return put(key, value, false);
        }

        public Builder<K, V> put(K key, V value, boolean mainResult) {
            if (key == null || value == null) return this;

            if (reverseMap.containsKey(value)) return this;

            // 添加新条目
            forwardMap.put(key, value);
            reverseMap.put(value, key);
            if (mainResult) mainResultMap.put(key, value);
            return this;
        }

        // 批量添加（可链式调用）
        public Builder<K, V> putAll(K key, Iterable<? extends V> values) {
            for (V value : values) {
                put(key, value, false);
            }
            return this;
        }

        public Builder<K, V> setMainResult(K key, V value) {
            if (key == null || value == null) return this;
            if (reverseMap.containsKey(value)) {
                mainResultMap.put(key, value);
            }
            return this;
        }

        public boolean contains(V value) {
            return reverseMap.containsKey(value);
        }

        public boolean containsKey(K key) {
            return forwardMap.containsKey(key);
        }

        public boolean isEmpty() {
            return forwardMap.isEmpty();
        }

        public Builder<K, V> clear() {
            forwardMap.clear();
            reverseMap.clear();
            mainResultMap.clear();
            return this;
        }

        public Builder<K, V> foreach(BiConsumer<K, V> consumer) {
            forwardMap.forEach(consumer);
            return this;
        }

        public <T> Builder<K, V> merge(Builder<K, T> value, Function<T,V> keyMapper) {
            if (value == null || value.isEmpty() || value == this) return this;
            value.foreach((k,t) -> put(k, keyMapper.apply(t)));
            value.mainResultMap.forEach((k,t) -> put(k, keyMapper.apply(t)));
            return this;
        }

        public Builder<K, V> merge(Builder<K, V> value) {
            if (value == null || value.isEmpty() || value == this) return this;
            value.foreach(this::put);
            mainResultMap.putAll(value.mainResultMap);
            return this;
        }

        public <T> Builder<K, V> merge(ImmutableBiMultiMap<K, T> value, Function<T,V> keyMapper) {
            if (value == null || value.isEmpty()) return this;
            value.foreach(((k, t) -> put(k, keyMapper.apply(t))));
            value.mainResultMap.forEach((k,t) -> put(k, keyMapper.apply(t)));
            return this;
        }

        public Builder<K, V> merge(ImmutableBiMultiMap<K, V> value) {
            if (value == null || value.isEmpty()) return this;
            value.foreach(this::put);
            mainResultMap.putAll(value.mainResultMap);
            return this;
        }

        // 构建不可变实例
        public ImmutableBiMultiMap<K, V> build() {
            return new ImmutableBiMultiMap<>(this);
        }
    }
}
