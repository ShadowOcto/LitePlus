package me.rhys.base.util.container;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

public class MapContainer<K, V> {

    @Setter
    @Getter
    private final Map<K, V> map = new HashMap<>();

    public void put(K key, V value) {
        map.put(key, value);
    }

    public void pop(K key) {
        map.remove(key);
    }

    public V get(K key) {
        return map.get(key);
    }

}